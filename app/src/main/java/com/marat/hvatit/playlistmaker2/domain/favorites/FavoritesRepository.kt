package com.marat.hvatit.playlistmaker2.domain.favorites

import com.marat.hvatit.playlistmaker2.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    fun medialibTracks(): Flow<List<Track>>

    suspend fun saveFavoriteTrack(track: Track)

    suspend fun deleteTrack(track: Track)

}