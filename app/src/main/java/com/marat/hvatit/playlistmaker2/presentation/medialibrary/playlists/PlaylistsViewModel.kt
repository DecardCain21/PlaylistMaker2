package com.marat.hvatit.playlistmaker2.presentation.medialibrary.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marat.hvatit.playlistmaker2.domain.api.usecase.GetPlaylistsUseCase
import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val getPlaylistsUseCase: GetPlaylistsUseCase) : ViewModel() {

    private var playlistsState: PlaylistsState = PlaylistsState.Data(emptyList())
    private var loadingPlaylistsData = MutableLiveData(playlistsState)

    fun getPlaylistsState(): LiveData<PlaylistsState> = loadingPlaylistsData

    fun getPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            getPlaylistsUseCase.execute().collect { playlists ->
                setDataState(playlists)
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