package com.marat.hvatit.playlistmaker2.presentation.audioplayer.controller

import android.media.MediaPlayer
import android.util.Log
import com.marat.hvatit.playlistmaker2.domain.api.AudioPlayerCallback
import com.marat.hvatit.playlistmaker2.presentation.audioplayer.AudioPlayerController
import com.marat.hvatit.playlistmaker2.presentation.audioplayer.MediaPlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerControllerImpl(
    private val previewUrl: String,
    private val activityCallBack: AudioPlayerCallback
) : AudioPlayerController {


    private val mediaPlayer = MediaPlayer()
    private var playerState: MediaPlayerState = MediaPlayerState.Default

    override fun stateControl(): MediaPlayerState {
        when (playerState) {
            is MediaPlayerState.Default -> {
                preparePlayer(previewUrl)
            }

            is MediaPlayerState.Prepared -> {
                startPlayer()
            }

            is MediaPlayerState.Paused -> {
                startPlayer()
            }

            is MediaPlayerState.Playing -> {
                pausePlayer()
            }
        }
        Log.e("MediaState","stateControl():$playerState")
        return playerState
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = MediaPlayerState.Prepared
            activityCallBack.playerPrepared()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = MediaPlayerState.Playing(getCurrentTime())
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.seekTo(0)
            playerState = MediaPlayerState.Prepared
            activityCallBack.trackIsDone()
        }
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = MediaPlayerState.Paused
    }

    override fun getCurrentTime(): String {
        //Log.e("MediaState", "getCurrentTime:${this.mediaPlayer.currentPosition}")
        Log.e("MediaState", "getCurrentTime,mediaplayer hashcode${mediaPlayer.hashCode()}")
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
    }

    override fun destroyActivity() {
        mediaPlayer.release()
    }

    override fun pauseActivity() {
        pausePlayer()
    }
}