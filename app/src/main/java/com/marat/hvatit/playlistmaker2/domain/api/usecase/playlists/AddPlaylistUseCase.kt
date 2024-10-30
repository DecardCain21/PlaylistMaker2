package com.marat.hvatit.playlistmaker2.domain.api.usecase.playlists

import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import com.marat.hvatit.playlistmaker2.domain.api.repository.PlaylistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddPlaylistUseCase(private val playlistsRepository: PlaylistsRepository) {
    suspend fun execute(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            playlistsRepository.savePlaylist(playlist)
        }
    }

    suspend fun getId(): Int {
        var listIds = mutableListOf<Int>()
        withContext(Dispatchers.IO) {
            playlistsRepository.getPlaylistsIds().collect { ids -> listIds.addAll(ids) }
        }
        return 0
    }
}