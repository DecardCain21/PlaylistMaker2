package com.marat.hvatit.playlistmaker2.domain.api.repository

import com.marat.hvatit.playlistmaker2.domain.models.Track
import com.marat.hvatit.playlistmaker2.presentation.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTrackCoroutine(expression: String): Flow<Resource<List<Track>>>

}