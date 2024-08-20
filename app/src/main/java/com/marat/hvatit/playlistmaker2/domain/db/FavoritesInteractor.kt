package com.marat.hvatit.playlistmaker2.domain.db

import com.marat.hvatit.playlistmaker2.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    fun addFavorite(): Flow<List<Track>>

    suspend fun saveFavoriteTrack(track: Track)

    suspend fun deleteFavorite(track: Track)
}