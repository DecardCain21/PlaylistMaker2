package com.marat.hvatit.playlistmaker2.domain.api.usecase.playlisttracks

import com.marat.hvatit.playlistmaker2.domain.api.repository.PlaylistsRepository

class DeletePlaylistTrackNoRefUseCase(private val repository: PlaylistsRepository) {
    suspend fun execute(playlistTrackId: String) {
        repository.deletePlaylistTrackNoRef(playlistTrackId = playlistTrackId)
    }

}