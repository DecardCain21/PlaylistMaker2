package com.marat.hvatit.playlistmaker2.presentation.audioplayer

sealed interface FavoriteState {
    data class IsFavorite(var favorite: Boolean) : FavoriteState
}