package com.marat.hvatit.playlistmaker2.domain.api.interactors

import com.marat.hvatit.playlistmaker2.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {

    fun searchTrack(expression: String, consumer: TrackConsumer)

    fun searchTrackCoroutine(expression: String): Flow<Pair<List<Track>?, String?>>

    interface TrackConsumer {
        fun consume(foundTrack: List<Track>?, errorMessage: String?)
    }

}