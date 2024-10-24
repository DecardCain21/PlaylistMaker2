package com.marat.hvatit.playlistmaker2.domain.api.usecase

import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import com.marat.hvatit.playlistmaker2.domain.playlists.PlaylistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeletePlaylistUseCase(private val repository: PlaylistsRepository) {
    suspend fun execute(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            repository.deletePlaylist(playlist = playlist)
        }
    }
}