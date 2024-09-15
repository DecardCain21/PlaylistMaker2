package com.marat.hvatit.playlistmaker2.presentation.medialibrary.playlists

import com.marat.hvatit.playlistmaker2.domain.models.Track

sealed interface PlaylistsState {
    data class Data(val data: List<Track>) : PlaylistsState
    object EmptyState : PlaylistsState
}