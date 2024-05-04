package com.marat.hvatit.playlistmaker2.presentation.audioplayer

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.marat.hvatit.playlistmaker2.domain.api.interactors.AudioPlayerInteractor

class AudioViewModel(private val interactor: AudioPlayerInteractor) : ViewModel() {

    private var playerState: MediaPlayerState = MediaPlayerState.Default

    /*private var currentState: MediaPlayerState = MediaPlayerState.Prepared*/
    private var loadingLiveData = MutableLiveData(playerState)

    private val handler = Handler(Looper.getMainLooper())
    private val timerRunnable: Runnable = Runnable { startTimer() }


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

    fun trackIsDone() {
        stopTimer()
        loadingLiveData.postValue(MediaPlayerState.Paused)
    }

    private fun startTimer() {
        handler.removeCallbacks(timerRunnable)
        //loadingLiveData.postValue(MediaPlayerState.Playing(interactor.updateTimer()))
        loadingLiveData.value = MediaPlayerState.Playing(interactor.updateTimer())
        Log.e("MediaState", "startTimer():${loadingLiveData.value}")
        handler.postDelayed(timerRunnable, 1000L)
    }

    private fun stopTimer() {
        handler.removeCallbacks(timerRunnable)
        //loadingLiveData.postValue(MediaPlayerState.Paused)
        loadingLiveData.value = MediaPlayerState.Paused
    }

    companion object {
        fun getViewModelFactory(interactor: AudioPlayerInteractor): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    AudioViewModel(interactor)
                }
            }
    }

}