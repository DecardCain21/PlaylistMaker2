package com.marat.hvatit.playlistmaker2.presentation.audioplayer

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marat.hvatit.playlistmaker2.domain.api.AudioPlayerCallback
import com.marat.hvatit.playlistmaker2.domain.api.interactors.AudioPlayerInteractor

class AudioViewModel(previewUrl: String, private val interactor: AudioPlayerInteractor) :
    ViewModel(),
    AudioPlayerCallback {

    private var playerState: MediaPlayerState = MediaPlayerState.Default
    private var loadingLiveData = MutableLiveData(playerState)

    private val handler = Handler(Looper.getMainLooper())
    private val timerRunnable: Runnable = Runnable { startTimer() }

    init {
        interactor.setPreviewUrl(previewUrl)
        interactor.setCallback(this)
    }

    fun getLoadingLiveData(): LiveData<MediaPlayerState> = loadingLiveData

    fun playbackControl() {
        loadingLiveData.value = interactor.playbackControl()
        Log.e("MediaState", "loadingLiveData:${loadingLiveData.value}")
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
        handler.removeCallbacks(timerRunnable)
        loadingLiveData.value = MediaPlayerState.Playing(interactor.updateTimer())
        Log.e("MediaState", "startTimer():${loadingLiveData.value}")
        handler.postDelayed(timerRunnable, 1000L)
    }

    private fun stopTimer() {
        handler.removeCallbacks(timerRunnable)
        loadingLiveData.value = MediaPlayerState.Paused
    }

    override fun trackIsDone() {
        stopTimer()
        loadingLiveData.postValue(MediaPlayerState.Completed())
    }

    override fun playerPrepared() {
        loadingLiveData.value = MediaPlayerState.Prepared
    }

}