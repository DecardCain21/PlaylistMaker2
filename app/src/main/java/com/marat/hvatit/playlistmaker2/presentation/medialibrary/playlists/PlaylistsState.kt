package com.marat.hvatit.playlistmaker2.presentation.medialibrary.playlists

import com.marat.hvatit.playlistmaker2.domain.models.Playlist

sealed interface PlaylistsState {
    data class Data(val data: List<Playlist>) : PlaylistsState
    object EmptyState : PlaylistsState
}