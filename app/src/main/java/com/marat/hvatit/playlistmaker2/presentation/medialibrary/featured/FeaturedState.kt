package com.marat.hvatit.playlistmaker2.presentation.medialibrary.featured

import com.marat.hvatit.playlistmaker2.domain.models.Track

sealed interface FeaturedState {
    data class Data(val data: List<Track>) : FeaturedState

    object EmptyState : FeaturedState
}