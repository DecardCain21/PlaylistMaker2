package com.marat.hvatit.playlistmaker2.presentation.audioplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marat.hvatit.playlistmaker2.domain.api.AudioPlayerCallback
import com.marat.hvatit.playlistmaker2.domain.api.interactors.AudioPlayerInteractor
import com.marat.hvatit.playlistmaker2.domain.db.FavoritesInteractor
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
    private val interactorDb: FavoritesInteractor
) :
    ViewModel(),
    AudioPlayerCallback {

    private var playerState: MediaPlayerState = MediaPlayerState.Default
    private var loadingLiveData = MutableLiveData(playerState)
    private var timerJob: Job? = null
    private var favoriteState: FavoriteState = FavoriteState.IsFavorite(false)
    private var loadingFavoriteData = MutableLiveData(favoriteState)

    companion object {
        private const val TIMER_DELAY = 300L
    }

    init {
        interactor.setPreviewUrl(previewUrl)
        interactor.setCallback(this)

    }

    fun getFavoriteState():LiveData<FavoriteState> = loadingFavoriteData
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
            interactorDb.addFavorite().catch { exception -> changeFavorite(false) }
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

    fun defaultFavoriteState(track: Track){
        viewModelScope.launch {
            changeFavorite(interactorDb.isFavorite(track))
        }
    }

    private fun changeFavorite(boolean: Boolean) {
        loadingFavoriteData.postValue(FavoriteState.IsFavorite(boolean))
    }

    private suspend fun deleteTrackDb(track: Track) {
        interactorDb.deleteFavorite(track)
    }

    private suspend fun saveTrackDb(track: Track) {
        interactorDb.saveFavoriteTrack(track)
    }

}