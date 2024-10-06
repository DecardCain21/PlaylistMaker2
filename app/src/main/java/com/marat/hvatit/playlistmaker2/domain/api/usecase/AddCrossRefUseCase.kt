package com.marat.hvatit.playlistmaker2.domain.api.usecase

import com.marat.hvatit.playlistmaker2.domain.playlists.PlaylistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddCrossRefUseCase(private val repository: PlaylistsRepository) {
    suspend fun execute(playlistId: String, trackId: String) {
        withContext(Dispatchers.IO) {
            repository.addCrossRef(playlistId = playlistId, trackId = trackId)
        }
    }
}