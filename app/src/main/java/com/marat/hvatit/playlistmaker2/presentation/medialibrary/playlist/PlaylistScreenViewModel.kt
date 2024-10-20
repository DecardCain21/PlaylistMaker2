package com.marat.hvatit.playlistmaker2.presentation.medialibrary.playlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marat.hvatit.playlistmaker2.domain.api.usecase.DeletePlaylistCrossRefUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.DeletePlaylistTrackNoRefUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.GetPlaylistTracksUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.UpdatePlaylistUseCase
import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import com.marat.hvatit.playlistmaker2.domain.models.Track
import kotlinx.coroutines.launch

class PlaylistScreenViewModel(
    private val getPlaylistTracksUseCase: GetPlaylistTracksUseCase,
    private val deletePlaylistCrossRefUseCase: DeletePlaylistCrossRefUseCase,
    private val deletePlaylistTrackNoRefUseCase: DeletePlaylistTrackNoRefUseCase,
    private val updatePlaylistUseCase: UpdatePlaylistUseCase
) :
    ViewModel() {

    private var tracksState: PlaylistTracksState = PlaylistTracksState.Data(emptyList())
    private var loadingTracksData = MutableLiveData(tracksState)
    fun getTracksState(): LiveData<PlaylistTracksState> = loadingTracksData

    private var loadingTracksVolume = MutableLiveData(0)
    private var loadingTracksSize = MutableLiveData(0)
    fun getTracksVolume():LiveData<Int> = loadingTracksVolume
    fun getTracksSize():LiveData<Int> = loadingTracksSize

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
            deletePlaylistCrossRefUseCase.execute(playlistId = playlist.playlistId, trackId = trackId)
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

    private fun setState(data: List<Track>) {
        loadingTracksData.postValue(PlaylistTracksState.Data(data))
        var result = 0
        for (i in data){
            result+=i.trackTimeMillis.toInt()
        }
        loadingTracksVolume.postValue(result)
        loadingTracksSize.postValue(data.size)
    }
}