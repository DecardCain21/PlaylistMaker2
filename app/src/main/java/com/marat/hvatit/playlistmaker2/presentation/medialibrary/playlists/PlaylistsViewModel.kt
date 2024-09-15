package com.marat.hvatit.playlistmaker2.presentation.medialibrary.playlists

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import com.marat.hvatit.playlistmaker2.domain.playlists.PlaylistsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val interactor: PlaylistsInteractor) : ViewModel() {

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

    fun getPlaylists(): List<Playlist> {
        var result: List<Playlist> = mutableListOf<Playlist>()
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getPlaylists().collect { playlists ->
                result = playlists
                Log.e("Playlists", "ViewModel,getPlaylists:$result")
            }
        }
        return result
    }
}