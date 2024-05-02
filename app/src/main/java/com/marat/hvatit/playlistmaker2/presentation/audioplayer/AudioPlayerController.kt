package com.marat.hvatit.playlistmaker2.presentation.audioplayer

interface AudioPlayerController {

    fun stateControl(): MediaPlayerState

    fun getCurrentTime(): String

    fun destroyActivity()

    fun pauseActivity()

}