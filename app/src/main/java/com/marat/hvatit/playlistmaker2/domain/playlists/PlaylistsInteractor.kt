package com.marat.hvatit.playlistmaker2.domain.playlists

import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    suspend fun savePlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)
    fun getPlaylists() : Flow<List<Playlist>>

}