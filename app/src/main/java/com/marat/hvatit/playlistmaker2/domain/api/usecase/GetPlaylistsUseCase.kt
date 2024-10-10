package com.marat.hvatit.playlistmaker2.domain.api.usecase

import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import com.marat.hvatit.playlistmaker2.domain.playlists.PlaylistsRepository
import kotlinx.coroutines.flow.Flow

class GetPlaylistsUseCase(private val repository: PlaylistsRepository) {
    fun execute(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }
}