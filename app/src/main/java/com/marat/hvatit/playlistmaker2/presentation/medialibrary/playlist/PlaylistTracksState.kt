package com.marat.hvatit.playlistmaker2.presentation.medialibrary.playlist

import com.marat.hvatit.playlistmaker2.domain.models.Track

sealed interface PlaylistTracksState {
    data class Data(val data: List<Track>) : PlaylistTracksState

    object EmptyState : PlaylistTracksState
}