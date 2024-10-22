package com.marat.hvatit.playlistmaker2.domain.api.usecase

import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import com.marat.hvatit.playlistmaker2.domain.playlists.PlaylistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetPlaylistByIdUseCase(private val repository: PlaylistsRepository) {
    suspend fun execute(playlistId: String): Playlist = withContext(Dispatchers.IO) {
        repository.getPlaylistById(playlistId = playlistId)
    }
}