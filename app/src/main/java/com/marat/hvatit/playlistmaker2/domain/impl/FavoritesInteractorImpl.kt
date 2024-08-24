package com.marat.hvatit.playlistmaker2.domain.impl

import com.marat.hvatit.playlistmaker2.data.db.FavoritesInteractor
import com.marat.hvatit.playlistmaker2.data.db.FavoritesRepository
import com.marat.hvatit.playlistmaker2.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FavoritesInteractorImpl(private val repositoryImpl: FavoritesRepository) :
    FavoritesInteractor {
    override fun addFavorite(): Flow<List<Track>> {
        return repositoryImpl.medialibTracks()
    }

    override suspend fun saveFavoriteTrack(track: Track) {
        repositoryImpl.saveFavoriteTrack(track)
    }

    override suspend fun deleteFavorite(track: Track) {
        repositoryImpl.deleteTrack(track)
    }

     override suspend fun isFavorite(track: Track): Boolean {
        val trackId = track.trackId
        var result: Boolean = false
        withContext(Dispatchers.IO) {
            addFavorite()
                .map { tracks -> tracks.any { it.trackId == trackId } }
                .collect { isFavorite ->
                    result = isFavorite
                }

        }
        return result
    }
}
