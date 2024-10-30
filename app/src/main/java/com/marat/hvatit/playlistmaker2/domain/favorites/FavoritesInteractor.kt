package com.marat.hvatit.playlistmaker2.domain.favorites

import com.marat.hvatit.playlistmaker2.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {

    suspend fun saveFavoriteTrack(track: Track)

    suspend fun deleteFavorite(track: Track)
}