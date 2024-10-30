package com.marat.hvatit.playlistmaker2.presentation.audioplayer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marat.hvatit.playlistmaker2.domain.api.AudioPlayerCallback
import com.marat.hvatit.playlistmaker2.domain.api.interactors.AudioPlayerInteractor
import com.marat.hvatit.playlistmaker2.domain.api.usecase.AddCrossRefUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.AddPlaylistTrackUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.GetPlaylistTracksUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.GetPlaylistsUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.UpdatePlaylistUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.tracks.AddTrackUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.tracks.DeleteTrackUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.tracks.GetFavoriteTracksUseCase
import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import com.marat.hvatit.playlistmaker2.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AudioViewModel(
    previewUrl: String,
    private val interactor: AudioPlayerInteractor,
    private val getPlaylistsUseCase: GetPlaylistsUseCase,
    private val addCrossRefUseCase: AddCrossRefUseCase,
    private val getPlaylistTracksUseCase: GetPlaylistTracksUseCase,
    private val addPlaylistTrackUseCase: AddPlaylistTrackUseCase,
    private val updatePlaylistUseCase: UpdatePlaylistUseCase,
    private val getFavoriteTracksUseCase: GetFavoriteTracksUseCase,
    private val addTrackUseCase: AddTrackUseCase,
    private val deleteTrackUseCase: DeleteTrackUseCase
) :
    ViewModel(),
    AudioPlayerCallback {

    private var playerState: MediaPlayerState = MediaPlayerState.Default
    private var loadingLiveData = MutableLiveData(playerState)
    private var timerJob: Job? = null
    private var favoriteState: FavoriteState = FavoriteState.IsFavorite(false)
    private var loadingFavoriteData = MutableLiveData(favoriteState)
    private var playlistsState: BottomPlaylistsState = BottomPlaylistsState.Data(emptyList())
    private var loadingPlaylistsData = MutableLiveData(playlistsState)

    private var addToPlaylist = MutableLiveData(false)
    fun getPlaylistsState(): LiveData<BottomPlaylistsState> = loadingPlaylistsData

    companion object {
        private const val TIMER_DELAY = 300L
    }

    init {
        interactor.setPreviewUrl(previewUrl)
        interactor.setCallback(this)
        playbackControl()

    }

    fun getFavoriteState(): LiveData<FavoriteState> = loadingFavoriteData
    fun getLoadingLiveData(): LiveData<MediaPlayerState> = loadingLiveData

    fun playbackControl() {
        var result = interactor.playbackControl()
        if (result is MediaPlayerState.Disconnected) {
            loadingLiveData.postValue(interactor.playbackControl())
            // установка значения LiveData из фонового потока
        }
        loadingLiveData.value = result
        //установка значения LiveData из текущего потока
        //Log.e("MediaState", "loadingLiveData:${loadingLiveData.value}")
        if (loadingLiveData.value is MediaPlayerState.Playing) {
            startTimer()
        } else {
            stopTimer()
        }
    }

    fun onPausePlayer() {
        stopTimer()
        interactor.stopPlayer()
        loadingLiveData.postValue(MediaPlayerState.Paused)
    }

    fun onDestroyPlayer() {
        stopTimer()
        interactor.destroyPlayer()
    }

    private fun startTimer() {
        loadingLiveData.value = MediaPlayerState.Playing(interactor.updateTimer())
        timerJob = viewModelScope.launch {
            delay(TIMER_DELAY)
            startTimer()
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        loadingLiveData.value = MediaPlayerState.Paused
    }

    override fun trackIsDone() {
        stopTimer()
        loadingLiveData.postValue(MediaPlayerState.Completed())
    }

    override fun playerPrepared() {
        loadingLiveData.value = MediaPlayerState.Prepared
    }

    fun setFavoriteState(track: Track) {
        val trackId = track.trackId
        viewModelScope.launch(Dispatchers.IO) {
            getFavoriteTracksUseCase.execute().catch { exception -> changeFavorite(false) }
                .map { tracks -> tracks.any { it.trackId == trackId } }
                .collect { isFavorite ->
                    if (isFavorite) {
                        deleteTrackDb(track)
                        changeFavorite(false)
                    } else {
                        saveTrackDb(track)
                        changeFavorite(true)
                    }
                }
        }

    }

    fun defaultFavoriteState(track: Track) {
        viewModelScope.launch {
            changeFavorite(getFavoriteTracksUseCase.isFavorite(track))
        }
    }

    private fun changeFavorite(boolean: Boolean) {
        loadingFavoriteData.postValue(FavoriteState.IsFavorite(boolean))
    }

    private suspend fun deleteTrackDb(track: Track) {
        //interactorDb.deleteFavorite(track)
        deleteTrackUseCase.execute(track)
    }

    private suspend fun saveTrackDb(track: Track) {
        //interactorDb.saveFavoriteTrack(track)
        addTrackUseCase.execute(track)
    }

    fun getPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            getPlaylistsUseCase.execute().collect { playlists ->
                setDataState(playlists)
            }
        }
    }

    fun addTrackToPlaylist(
        playlist: Playlist,
        track: Track,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val isTrackExist = getPlaylistTracksUseCase.execute(playlistId = playlist.playlistId)
                .any { it.trackId == track.trackId }
            if (isTrackExist) {
                onError()
            } else {
                Log.e("CrossRef", "postValue${addToPlaylist.value}")
                updatePlaylists(playlist, track.trackId)
                addPlaylistTrack(track)
                onSuccess()
            }
        }
    }

    private suspend fun updatePlaylists(playlist: Playlist, trackId: String) {
        addCrossRefUseCase.execute(playlist.playlistId, trackId)
        val newSize = playlist.playlistSize.toInt() + 1
        updatePlaylistUseCase.updateSize(
            playlistId = playlist.playlistId,
            newSize = newSize.toString()
        )
    }

    private suspend fun addPlaylistTrack(track: Track) {
        addPlaylistTrackUseCase.execute(track)
    }

    private fun setDataState(data: List<Playlist>) {
        if (data.isEmpty()) {
            loadingPlaylistsData.postValue(BottomPlaylistsState.EmptyState)
        } else {
            loadingPlaylistsData.postValue(BottomPlaylistsState.Data(data))
        }
    }

}