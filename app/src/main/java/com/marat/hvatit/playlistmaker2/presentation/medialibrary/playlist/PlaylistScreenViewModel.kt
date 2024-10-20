package com.marat.hvatit.playlistmaker2.presentation.medialibrary.playlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marat.hvatit.playlistmaker2.domain.api.usecase.DeletePlaylistCrossRefUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.DeletePlaylistTrackNoRefUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.DeletePlaylistUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.GetPlaylistTracksUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.UpdatePlaylistUseCase
import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import com.marat.hvatit.playlistmaker2.domain.models.Track
import com.marat.hvatit.playlistmaker2.presentation.settings.ActionFilter
import com.marat.hvatit.playlistmaker2.presentation.settings.IntentNavigator
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class PlaylistScreenViewModel(
    private val intentNavigator: IntentNavigator,
    private val getPlaylistTracksUseCase: GetPlaylistTracksUseCase,
    private val deletePlaylistCrossRefUseCase: DeletePlaylistCrossRefUseCase,
    private val deletePlaylistTrackNoRefUseCase: DeletePlaylistTrackNoRefUseCase,
    private val updatePlaylistUseCase: UpdatePlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase
) :
    ViewModel() {

    private var tracksState: PlaylistTracksState = PlaylistTracksState.Data(emptyList())
    private var loadingTracksData = MutableLiveData(tracksState)
    fun getTracksState(): LiveData<PlaylistTracksState> = loadingTracksData

    private var loadingTracksVolume = MutableLiveData(0)
    private var loadingTracksSize = MutableLiveData(0)
    fun getTracksVolume(): LiveData<Int> = loadingTracksVolume
    fun getTracksSize(): LiveData<Int> = loadingTracksSize
    private var saveTracks: List<Track> = emptyList()

    fun setShare(
        playlistName: String,
        playlistDescription: String,
        onError: () -> Unit
    ) {
        if (saveTracks.isNotEmpty()) {
            intentNavigator.createIntent(
                ActionFilter.SHARE,
                message = createMessage(playlistName, playlistDescription)
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
        viewModelScope.launch {
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
            // Теперь вы можете удалить плейлист
            deletePlaylistUseCase.execute(playlist)
        }
    }

    private fun setState(data: List<Track>) {
        saveTracks = data
        loadingTracksData.postValue(PlaylistTracksState.Data(data))
        var result = 0
        for (i in data) {
            result += i.trackTimeMillis.toInt()
        }
        loadingTracksVolume.postValue(result)
        loadingTracksSize.postValue(data.size)
    }

    private fun createMessage(
        playlistName: String, playlistDescription: String,
    ): String {
        val trackCount = saveTracks.size
        val tracksList = saveTracks.joinToString("\n") {
            "${saveTracks.indexOf(it) + 1}. ${it.artistName} - ${it.trackName} (${it.trackTimeMillis})"
        }
        return """
        $playlistName
        $playlistDescription
        [$trackCount треков]
        $tracksList
        """.trimIndent()
    }
}