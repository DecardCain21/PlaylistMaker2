package com.marat.hvatit.playlistmaker2.data

import com.marat.hvatit.playlistmaker2.data.db.AppDatabase
import com.marat.hvatit.playlistmaker2.data.db.converters.PlaylistDbConvertor
import com.marat.hvatit.playlistmaker2.data.db.entity.PlaylistEntity
import com.marat.hvatit.playlistmaker2.data.db.entity.PlaylistWithTrack
import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import com.marat.hvatit.playlistmaker2.domain.models.Track
import com.marat.hvatit.playlistmaker2.domain.api.repository.PlaylistsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor
) : PlaylistsRepository {

    override suspend fun getPlaylistsWithTrack(playlistId: String): List<Track> {
        val playlistsWithTrack = appDatabase.trackDao().getPlaylistWorker(playlistId.toInt())
        return convertFromPlaylistWithTrack(playlistsWithTrack)
    }


    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.trackDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override fun getPlaylistsIds(): Flow<List<Int>> = flow {
        val listIds = appDatabase.trackDao().getPlaylistIds()
        emit(listIds)
    }

    override suspend fun addCrossRef(playlistId: String, trackId: String) {
        appDatabase.trackDao().insertPlaylistCrossRef(
            playlistDbConvertor.convertToCrossRef(
                playlistId = playlistId,
                trackId = trackId
            )
        )
    }

    override suspend fun savePlaylist(playlist: Playlist) {
        appDatabase.trackDao().insertPlaylist(convertToPlaylistEntity(playlist))
    }

    override suspend fun savePlaylistTrack(track: Track) {
        appDatabase.trackDao().insertPlaylistTrack(playlistDbConvertor.convertToEntity(track))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        appDatabase.trackDao().deletePlaylist(convertToPlaylistEntity(playlist))
    }

    override suspend fun updatePlaylistSize(playlistId: String, newSize: String) {
        appDatabase.trackDao().updatePlaylistSize(playlistId = playlistId.toInt(), newSize)
    }

    override suspend fun deletePlaylistCrossRef(playlistId: String, trackId: String) {
        appDatabase.trackDao()
            .deletePlaylistCrossRef(playlistId = playlistId.toInt(), trackId = trackId)
    }

    override suspend fun deletePlaylistTrackNoRef(playlistTrackId: String) {
        appDatabase.trackDao()
            .deletePlaylistTrackIfNoReferences(playlistTrackId = playlistTrackId.toInt())
    }

    override suspend fun getPlaylistById(playlistId: String): Playlist {
        return playlistDbConvertor.map(appDatabase.trackDao().getPlaylistById(playlistId.toInt()))
    }

    private fun convertFromPlaylistWithTrack(playlistWithTrack: PlaylistWithTrack): List<Track> {
        return playlistWithTrack.tracks.map {
            playlistDbConvertor.map(it)
        }
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConvertor.map(playlist) }
    }

    private fun convertToPlaylistEntity(playlist: Playlist): PlaylistEntity {
        return playlistDbConvertor.convertToEntity(playlist)
    }
}