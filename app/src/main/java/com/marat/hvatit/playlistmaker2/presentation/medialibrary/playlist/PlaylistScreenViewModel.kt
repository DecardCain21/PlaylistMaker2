package com.marat.hvatit.playlistmaker2.presentation.medialibrary.playlist

import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marat.hvatit.playlistmaker2.domain.api.usecase.DeletePlaylistCrossRefUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.DeletePlaylistTrackNoRefUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.DeletePlaylistUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.GetPlaylistByIdUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.GetPlaylistTracksUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.UpdatePlaylistUseCase
import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import com.marat.hvatit.playlistmaker2.domain.models.Track
import com.marat.hvatit.playlistmaker2.presentation.settings.ActionFilter
import com.marat.hvatit.playlistmaker2.presentation.settings.IntentNavigator
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlaylistScreenViewModel(
    private val intentNavigator: IntentNavigator,
    private val getPlaylistTracksUseCase: GetPlaylistTracksUseCase,
    private val deletePlaylistCrossRefUseCase: DeletePlaylistCrossRefUseCase,
    private val deletePlaylistTrackNoRefUseCase: DeletePlaylistTrackNoRefUseCase,
    private val updatePlaylistUseCase: UpdatePlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
    private val getPlaylistByIdUseCase: GetPlaylistByIdUseCase
) :
    ViewModel() {
    private val simpleDateFormat: SimpleDateFormat =
        SimpleDateFormat("mm:ss", Locale.getDefault())

    private var tracksState: PlaylistTracksState = PlaylistTracksState.Data(emptyList())
    private var loadingTracksData = MutableLiveData(tracksState)
    fun getTracksState(): LiveData<PlaylistTracksState> = loadingTracksData

    private var loadingTracksVolume = MutableLiveData(0)
    private var loadingTracksSize = MutableLiveData(0)
    fun getTracksVolume(): LiveData<Int> = loadingTracksVolume
    fun getTracksSize(): LiveData<Int> = loadingTracksSize
    private var saveTracks: List<Track> = emptyList()

    private var loadingSaveState = MutableLiveData(Playlist("", "", "", "", ""))
    fun setSaveState(playlist: Playlist) {
        Log.e("setSaveState", "$playlist")
        loadingSaveState.postValue(playlist)
    }

    fun getSavePlaylist(): LiveData<Playlist> = loadingSaveState

    fun setShare(
        playlistName: String,
        playlistDescription: String,
        playlistCount: String,
        onError: () -> Unit
    ) {
        if (saveTracks.isNotEmpty()) {
            intentNavigator.createIntent(
                ActionFilter.SHARE,
                message = createMessage(
                    playlistName,
                    playlistDescription,
                    playlistCount = playlistCount
                )
            )
        } else {
            onError()
        }
    }

    fun getTracksById(id: String) {
        viewModelScope.launch {
            val result = getPlaylistTracksUseCase.execute(
                playlistId = id
            )
            setState(result)
            Log.e("getTracks", "getTracksById$result")
        }
    }

    fun deletePlaylistCrossReference(playlist: Playlist, trackId: String) {
        viewModelScope.launch {
            deletePlaylistCrossRefUseCase.execute(
                playlistId = playlist.playlistId,
                trackId = trackId
            )
            val newSize = playlist.playlistSize.toInt() - 1
            updatePlaylistUseCase.updateSize(
                playlistId = playlist.playlistId,
                newSize = newSize.toString()
            )
        }
    }

    fun deletePlaylistTrackNoReferences(playlistTrackId: String) {
        viewModelScope.launch {
            deletePlaylistTrackNoRefUseCase.execute(playlistTrackId = playlistTrackId)
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        deleteImage(playlist.playlistCoverUrl)
        viewModelScope.launch {
            deletePlaylistUseCase.execute(playlist)
            if (saveTracks.isNotEmpty()) {
                val deleteTrackJobs = saveTracks.map { track ->
                    async {
                        deletePlaylistCrossRefUseCase.execute(
                            playlistId = playlist.playlistId,
                            trackId = track.trackId
                        )
                        deletePlaylistTrackNoRefUseCase.execute(playlistTrackId = track.trackId)
                    }
                }
                // Ждем завершения всех операций удаления треков
                deleteTrackJobs.forEach { it.await() }
            }
        }
    }

    private fun deleteImage(fileName: String) {
        val filePath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "Myalbum"
        )
        val fileToDelete = File(filePath, "$fileName.jpg")
        if (fileToDelete.exists()) {
            val deleted = fileToDelete.delete()
            if (deleted) {
                // Успешное удаление
                //Toast.makeText(requireContext(), "Изображение удалено", Toast.LENGTH_SHORT).show()
                Log.e("fileDeleted", "$deleted")
            } else {
                // Ошибка при удалении
                Log.e("fileDeleted", "$deleted")
                //Toast.makeText(requireContext(), "Ошибка при удалении изображения", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun resumeState(playlistId: String) {
        viewModelScope.launch {
            val result = getPlaylistByIdUseCase.execute(playlistId = playlistId)
            setSaveState(result)
        }
    }

    private fun setState(data: List<Track>) {
        val sortData = data.sortedByDescending { it.dateAdd }
        saveTracks = sortData
        if (data.isEmpty()) {
            loadingTracksData.postValue(PlaylistTracksState.EmptyState)
        } else {
            loadingTracksData.postValue(PlaylistTracksState.Data(sortData))
        }
        var result = 0
        for (i in sortData) {
            result += i.trackTimeMillis.toInt()
        }
        loadingTracksVolume.postValue(result)
        loadingTracksSize.postValue(data.size)
    }

    private fun createMessage(
        playlistName: String, playlistDescription: String, playlistCount: String
    ): String {
        //var result: Date? = volume?.let { Date(it.toLong()) }
        val tracksList = saveTracks.joinToString("\n") {
            "${saveTracks.indexOf(it) + 1}. ${it.artistName} - ${it.trackName} (${
                simpleDateFormat.format(
                    Date(it.trackTimeMillis.toLong())
                )
            })"
        }
        Log.e(
            "message",
            "\"\"\"\n" + "$playlistName\n" + "$playlistDescription\n" + "[$playlistCount]\n" + "$tracksList\n" + "\"\"\"".trimIndent()
        )
        return """
        $playlistName
        $playlistDescription
        [$playlistCount]
        $tracksList
        """.trimIndent()
    }
}