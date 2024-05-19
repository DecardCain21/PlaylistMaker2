package com.marat.hvatit.playlistmaker2.presentation.audioplayer.controller

import android.content.Context
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.marat.hvatit.playlistmaker2.creator.PlaylistMakerApp
import com.marat.hvatit.playlistmaker2.domain.api.AudioPlayerCallback
import com.marat.hvatit.playlistmaker2.presentation.audioplayer.AudioPlayerController
import com.marat.hvatit.playlistmaker2.presentation.audioplayer.MediaPlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerControllerImpl : AudioPlayerController {

    private val _activityCallBack: AudioPlayerCallback?
        get() = activityCallBack

    override var activityCallBack: AudioPlayerCallback? = null

    private val _previewUrl
        get() = previewUrl

    override var previewUrl: String? = null

    private val mediaPlayer = MediaPlayer()

    private var playerState: MediaPlayerState = MediaPlayerState.Default

    override fun stateControl(): MediaPlayerState {
        when (playerState) {

            is MediaPlayerState.Default -> {
                _previewUrl?.let { preparePlayer(it) }
            }

            is MediaPlayerState.Prepared -> {
                startPlayer()
                //playerState = MediaPlayerState.Paused
            }

            is MediaPlayerState.Paused -> {
                startPlayer()
            }

            is MediaPlayerState.Playing -> {
                pausePlayer()
            }

            is MediaPlayerState.Completed -> {
                startPlayer()
            }

            is MediaPlayerState.Disconnected -> {
                //nothing
            }
        }
        Log.e("MediaState", "stateControl():$playerState")
        return playerState
    }

    private fun preparePlayer(url: String) {
        if (isConnected()) {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playerState = MediaPlayerState.Prepared
                _activityCallBack?.playerPrepared()
            }
        } else {
            playerState = MediaPlayerState.Disconnected("error")
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = MediaPlayerState.Playing(getCurrentTime())
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.seekTo(0)
            playerState = MediaPlayerState.Completed()
            _activityCallBack?.trackIsDone()
        }
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = MediaPlayerState.Paused
    }

    override fun getCurrentTime(): String {
        //Log.e("MediaState", "getCurrentTime:${this.mediaPlayer.currentPosition}")
        //Log.e("MediaState", "getCurrentTime,mediaplayer hashcode${mediaPlayer.hashCode()}")
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
    }

    override fun destroyActivity() {
        mediaPlayer.release()
    }

    override fun pauseActivity() {
        pausePlayer()
    }

    private fun isConnected(): Boolean {
        val connectivityManager = PlaylistMakerApp.applicationContext().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val capabilities = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        } else {
            //если версия меньше 23
            return false
        }
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}