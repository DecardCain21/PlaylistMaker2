package com.marat.hvatit.playlistmaker2.domain.playlists

import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import com.marat.hvatit.playlistmaker2.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun getPlaylistsWithTrack(playlistId:String): Flow<List<Track>>

    fun getPlaylists(): Flow<List<Playlist>>

    fun getPlaylistsIds(): Flow<List<Int>>

    suspend fun addCrossRef(playlistId: String, trackId: String)

    suspend fun savePlaylist(playlist: Playlist)

    suspend fun savePlaylistTrack(track: Track)

    suspend fun deletePlaylist(playlist: Playlist)
}