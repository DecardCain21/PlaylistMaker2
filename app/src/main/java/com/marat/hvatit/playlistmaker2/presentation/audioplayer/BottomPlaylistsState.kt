package com.marat.hvatit.playlistmaker2.presentation.audioplayer

import com.marat.hvatit.playlistmaker2.domain.models.Playlist

sealed interface BottomPlaylistsState{
    data class Data(val data: List<Playlist>) : BottomPlaylistsState
    object EmptyState : BottomPlaylistsState
}