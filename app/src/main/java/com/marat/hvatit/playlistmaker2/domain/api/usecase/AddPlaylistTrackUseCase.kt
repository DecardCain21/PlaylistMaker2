package com.marat.hvatit.playlistmaker2.domain.api.usecase

import com.marat.hvatit.playlistmaker2.domain.models.Track
import com.marat.hvatit.playlistmaker2.domain.playlists.PlaylistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddPlaylistTrackUseCase(private val repository: PlaylistsRepository) {
    suspend fun execute(track: Track) {
        withContext(Dispatchers.IO) {
            repository.savePlaylistTrack(track)
        }
    }
}