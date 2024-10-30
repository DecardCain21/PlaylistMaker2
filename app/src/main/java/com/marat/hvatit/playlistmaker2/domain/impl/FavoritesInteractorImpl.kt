package com.marat.hvatit.playlistmaker2.domain.impl

import com.marat.hvatit.playlistmaker2.domain.favorites.FavoritesInteractor
import com.marat.hvatit.playlistmaker2.domain.favorites.FavoritesRepository
import com.marat.hvatit.playlistmaker2.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FavoritesInteractorImpl(private val repositoryImpl: FavoritesRepository) :
    FavoritesInteractor {
    override suspend fun saveFavoriteTrack(track: Track) {
        repositoryImpl.saveFavoriteTrack(track)
    }

    override suspend fun deleteFavorite(track: Track) {
        repositoryImpl.deleteTrack(track)
    }

}
