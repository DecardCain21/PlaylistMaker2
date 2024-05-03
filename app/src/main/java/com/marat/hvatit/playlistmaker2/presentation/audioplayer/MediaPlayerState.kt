package com.marat.hvatit.playlistmaker2.presentation.audioplayer

sealed interface MediaPlayerState {

    object Default : MediaPlayerState

    object Prepared : MediaPlayerState

    data class Playing(val currentTime: String) : MediaPlayerState

    object Paused : MediaPlayerState

}