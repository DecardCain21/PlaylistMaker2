package com.marat.hvatit.playlistmaker2.domain.api.usecase.tracks

import com.marat.hvatit.playlistmaker2.domain.api.repository.FavoritesRepository
import com.marat.hvatit.playlistmaker2.domain.models.Track

class DeleteTrackUseCase(private val repositoryImpl: FavoritesRepository) {
    suspend fun execute(track: Track) {
        repositoryImpl.deleteTrack(track)
    }

}