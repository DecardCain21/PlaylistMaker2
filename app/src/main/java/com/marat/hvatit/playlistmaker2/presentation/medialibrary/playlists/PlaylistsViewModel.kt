package com.marat.hvatit.playlistmaker2.presentation.medialibrary.playlists

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import com.marat.hvatit.playlistmaker2.domain.playlists.PlaylistsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val interactor: PlaylistsInteractor) : ViewModel() {

    private var playlistsState: PlaylistsState = PlaylistsState.Data(emptyList())
    private var loadingPlaylistsData = MutableLiveData(playlistsState)

    fun getPlaylistsState(): LiveData<PlaylistsState> = loadingPlaylistsData

    fun savePlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.savePlaylist(playlist)
        }

    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.deletePlaylist(playlist)
        }
    }

    fun getPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getPlaylists().collect { playlists ->
                setDataState(playlists)
                Log.e("Playlists", "ViewModel,getPlaylists:$playlists")
            }
        }
    }

    private fun setDataState(data: List<Playlist>) {
        if (data.isEmpty()) {
            loadingPlaylistsData.postValue(PlaylistsState.EmptyState)
        } else {
            loadingPlaylistsData.postValue(PlaylistsState.Data(data))
        }
    }
}