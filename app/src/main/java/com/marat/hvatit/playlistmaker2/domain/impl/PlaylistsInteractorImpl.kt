package com.marat.hvatit.playlistmaker2.domain.impl

import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import com.marat.hvatit.playlistmaker2.domain.playlists.PlaylistsInteractor
import com.marat.hvatit.playlistmaker2.domain.playlists.PlaylistsRepository
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val repository: PlaylistsRepository): PlaylistsInteractor {
    override suspend fun savePlaylist(playlist: Playlist) {
        repository.savePlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        repository.deletePlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }


}