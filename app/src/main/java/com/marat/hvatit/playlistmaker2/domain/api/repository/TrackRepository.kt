package com.marat.hvatit.playlistmaker2.domain.api.repository

import com.marat.hvatit.playlistmaker2.domain.models.Track
import com.marat.hvatit.playlistmaker2.presentation.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTrack(expression: String): Resource<List<Track>>

    fun searchTrackCoroutine(expression: String): Flow<Resource<List<Track>>>

}