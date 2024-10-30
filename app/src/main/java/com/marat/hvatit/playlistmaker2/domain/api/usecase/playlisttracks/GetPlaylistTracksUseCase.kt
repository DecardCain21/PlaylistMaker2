package com.marat.hvatit.playlistmaker2.domain.api.usecase.playlisttracks

import com.marat.hvatit.playlistmaker2.domain.models.Track
import com.marat.hvatit.playlistmaker2.domain.api.repository.PlaylistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetPlaylistTracksUseCase(private val repository: PlaylistsRepository) {
    suspend fun execute(playlistId: String): List<Track> = withContext(Dispatchers.IO) {
        repository.getPlaylistsWithTrack(playlistId)
    }
}