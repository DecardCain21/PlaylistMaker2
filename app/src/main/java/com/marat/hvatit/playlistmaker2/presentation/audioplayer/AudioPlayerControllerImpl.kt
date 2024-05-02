package com.marat.hvatit.playlistmaker2.presentation.audioplayer

import android.media.MediaPlayer
import com.marat.hvatit.playlistmaker2.domain.api.AudioPlayerCallback
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerControllerImpl(
    private val priviewUrl: String,
    private val activityCallBack: AudioPlayerCallback
) :
    AudioPlayerController {


    private var mediaPlayer = MediaPlayer()
    private var playerState: MediaPlayerState = MediaPlayerState.Default
    override fun stateControl(): MediaPlayerState {
        when (playerState) {
            MediaPlayerState.Default -> {
                preparePlayer(priviewUrl)
            }

            MediaPlayerState.Prepared -> {
                startPlayer()
            }

            is MediaPlayerState.Paused -> {
                startPlayer()
            }
            is MediaPlayerState.Playing -> {
                pausePlayer()
            }
        }
        return playerState
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = MediaPlayerState.Prepared
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
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
    }

    override fun destroyActivity() {
        mediaPlayer.release()
    }

    override fun pauseActivity() {
        pausePlayer()
    }
}