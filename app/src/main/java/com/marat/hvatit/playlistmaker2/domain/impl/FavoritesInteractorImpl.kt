package com.marat.hvatit.playlistmaker2.domain.impl

import com.marat.hvatit.playlistmaker2.domain.db.FavoritesInteractor
import com.marat.hvatit.playlistmaker2.domain.db.FavoritesRepository
import com.marat.hvatit.playlistmaker2.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val repositoryImpl: FavoritesRepository) :
    FavoritesInteractor {
    override fun favoritesTracks(): Flow<List<Track>> {
        return repositoryImpl.medialibTracks()
    }

    override suspend fun saveFavoriteTrack(track: Track) {
        repositoryImpl.saveFavoriteTrack(track)
    }
}