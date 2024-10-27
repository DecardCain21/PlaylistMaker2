package com.marat.hvatit.playlistmaker2.data

import android.util.Log
import com.marat.hvatit.playlistmaker2.data.db.AppDatabase
import com.marat.hvatit.playlistmaker2.data.db.converters.TrackDbConvertor
import com.marat.hvatit.playlistmaker2.data.db.entity.TrackEntity
import com.marat.hvatit.playlistmaker2.domain.favorites.FavoritesRepository
import com.marat.hvatit.playlistmaker2.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) : FavoritesRepository {
    override fun medialibTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun saveFavoriteTrack(track: Track) {
        appDatabase.trackDao().insertTrack(convertToTrackEntity(track))
    }

    override suspend fun deleteTrack(track: Track) {
        appDatabase.trackDao().deleteTrack(convertToTrackEntity(track))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }

    private fun convertToTrackEntity(track: Track): TrackEntity {
        return trackDbConvertor.convertToEntity(track)
    }


}