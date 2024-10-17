package com.marat.hvatit.playlistmaker2.presentation.medialibrary.playlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marat.hvatit.playlistmaker2.domain.api.usecase.GetPlaylistTracksUseCase
import com.marat.hvatit.playlistmaker2.domain.models.Track
import kotlinx.coroutines.launch

class PlaylistScreenViewModel(private val getPlaylistTracksUseCase: GetPlaylistTracksUseCase) :
    ViewModel() {

    private var tracksState: PlaylistTracksState = PlaylistTracksState.Data(emptyList())
    private var loadingTracksData = MutableLiveData(tracksState)
    fun getTracksState(): LiveData<PlaylistTracksState> = loadingTracksData

    fun getTracksById(id: String) {
        viewModelScope.launch {
            val result = getPlaylistTracksUseCase.execute(
                playlistId = id
            )
            setState(result)

            Log.e("getTracks", "getTracksById$result")
        }

    }

    private fun setState(data: List<Track>) {
        loadingTracksData.postValue(PlaylistTracksState.Data(data))
    }
}