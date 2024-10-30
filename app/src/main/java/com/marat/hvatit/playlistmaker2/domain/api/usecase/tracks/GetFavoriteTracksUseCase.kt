package com.marat.hvatit.playlistmaker2.domain.api.usecase.tracks

import com.marat.hvatit.playlistmaker2.domain.api.repository.FavoritesRepository
import com.marat.hvatit.playlistmaker2.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetFavoriteTracksUseCase(private val repositoryImpl: FavoritesRepository) {
    fun execute(): Flow<List<Track>> {
        return repositoryImpl.medialibTracks()
    }

    suspend fun isFavorite(track: Track): Boolean {
        val trackId = track.trackId
        var result: Boolean = false
        withContext(Dispatchers.IO) {
            execute()
                .map { tracks -> tracks.any { it.trackId == trackId } }
                .collect { isFavorite ->
                    result = isFavorite
                }

        }
        return result
    }
}