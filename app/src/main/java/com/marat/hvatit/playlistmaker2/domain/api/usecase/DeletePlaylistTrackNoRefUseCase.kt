package com.marat.hvatit.playlistmaker2.domain.api.usecase

import com.marat.hvatit.playlistmaker2.domain.playlists.PlaylistsRepository

class DeletePlaylistTrackNoRefUseCase(private val repository: PlaylistsRepository) {
    suspend fun execute(playlistTrackId: String) {
        repository.deletePlaylistTrackNoRef(playlistTrackId = playlistTrackId)
    }

}