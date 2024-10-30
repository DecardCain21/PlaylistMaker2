package com.marat.hvatit.playlistmaker2.domain.api.usecase.playlists

import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import com.marat.hvatit.playlistmaker2.domain.api.repository.PlaylistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeletePlaylistUseCase(private val repository: PlaylistsRepository) {
    suspend fun execute(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            repository.deletePlaylist(playlist = playlist)
        }
    }
}