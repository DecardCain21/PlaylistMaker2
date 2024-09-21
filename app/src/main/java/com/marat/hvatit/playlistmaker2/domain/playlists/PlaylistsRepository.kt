package com.marat.hvatit.playlistmaker2.domain.playlists

import com.marat.hvatit.playlistmaker2.data.db.entity.PlaylistWithTrack
import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun getMedialibPlaylists(playlist:Playlist): Flow<List<PlaylistWithTrack>>

    fun getPlaylists():Flow<List<Playlist>>

    fun getPlaylistsIds():Flow<List<Int>>

    suspend fun savePlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)
}