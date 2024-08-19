package com.marat.hvatit.playlistmaker2.presentation.audioplayer

sealed interface AudioPlayerState {
    data class IsFavorite(var favorite: Boolean)
}