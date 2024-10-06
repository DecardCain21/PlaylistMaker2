package com.marat.hvatit.playlistmaker2.domain.api.usecase

import com.marat.hvatit.playlistmaker2.domain.playlists.PlaylistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdatePlaylistUseCase(private val repository: PlaylistsRepository) {
    suspend fun updateSize(playlistId: String, newSize: String) {
        withContext(Dispatchers.IO) {
            repository.updatePlaylistSize(playlistId = playlistId, newSize = newSize)
        }
    }

}