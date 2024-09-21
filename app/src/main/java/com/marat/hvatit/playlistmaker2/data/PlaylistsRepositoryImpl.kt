package com.marat.hvatit.playlistmaker2.data

import android.util.Log
import com.marat.hvatit.playlistmaker2.data.db.AppDatabase
import com.marat.hvatit.playlistmaker2.data.db.converters.PlaylistDbConvertor
import com.marat.hvatit.playlistmaker2.data.db.entity.PlaylistEntity
import com.marat.hvatit.playlistmaker2.data.db.entity.PlaylistWithTrack
import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import com.marat.hvatit.playlistmaker2.domain.playlists.PlaylistsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor
) : PlaylistsRepository {

    override fun getMedialibPlaylists(playlist: Playlist): Flow<List<PlaylistWithTrack>> = flow {
        val playlists = appDatabase.trackDao().getPlaylistWorker(playlist.playlistId.toInt())
        Log.e("Playlists", "getMedialibPlaylists:$playlists")
        emit(listOf(playlists))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.trackDao().getPlaylists()
        Log.e("Playlists", "Repository,getPlaylists:$playlists")
        emit(convertFromPlaylistEntity(playlists))
    }

    override fun getPlaylistsIds(): Flow<List<Int>>  = flow{
        val listIds = appDatabase.trackDao().getPlaylistIds()
        emit(listIds)
    }

    override suspend fun savePlaylist(playlist: Playlist) {
        appDatabase.trackDao().insertPlaylist(convertToPlaylistEntity(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        appDatabase.trackDao().deletePlaylist(convertToPlaylistEntity(playlist))
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConvertor.map(playlist) }
    }

    private fun convertToPlaylistEntity(playlist: Playlist): PlaylistEntity {
        return playlistDbConvertor.convertToEntity(playlist)
    }
}