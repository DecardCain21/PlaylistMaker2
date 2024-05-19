package com.marat.hvatit.playlistmaker2.presentation.audioplayer

sealed interface MediaPlayerState {

    object Default : MediaPlayerState

    object Prepared : MediaPlayerState

    data class Playing(val currentTime: String) : MediaPlayerState

    object Paused : MediaPlayerState

    data class Completed(val currentTime: String = "00:00") : MediaPlayerState

    data class Disconnected(val message : String) : MediaPlayerState

}