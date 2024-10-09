package com.marat.hvatit.playlistmaker2.presentation.search

import com.marat.hvatit.playlistmaker2.domain.models.Track


sealed interface SearchState {
    data class Data(val foundTrack: List<Track>) : SearchState

    data class RestoreDataState(val tracks: List<Track>, val scrollPosition: Int) : SearchState
    class Disconnected(val message: Int) : SearchState
    class NothingToShow(val message: Int) : SearchState

    data class StartState(val cacheTracks: List<Track>) : SearchState

    object ClearState : SearchState

    object AllFine : SearchState

    object Download : SearchState
}