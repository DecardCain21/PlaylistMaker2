package com.marat.hvatit.playlistmaker2.data.db

import com.marat.hvatit.playlistmaker2.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    fun addFavorite(): Flow<List<Track>>

    suspend fun saveFavoriteTrack(track: Track)

    suspend fun deleteFavorite(track: Track)

    suspend fun isFavorite(track: Track): Boolean
}