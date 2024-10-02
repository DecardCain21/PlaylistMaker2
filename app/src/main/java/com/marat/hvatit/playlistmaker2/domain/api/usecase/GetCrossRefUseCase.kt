package com.marat.hvatit.playlistmaker2.domain.api.usecase

import com.marat.hvatit.playlistmaker2.domain.models.Track
import com.marat.hvatit.playlistmaker2.domain.playlists.PlaylistsRepository
import kotlinx.coroutines.flow.Flow

class GetCrossRefUseCase(private val repository: PlaylistsRepository) {
    suspend fun execute(playlistId: String): Flow<List<Track>> {
        return repository.getPlaylistsWithTrack(playlistId)
    }
}