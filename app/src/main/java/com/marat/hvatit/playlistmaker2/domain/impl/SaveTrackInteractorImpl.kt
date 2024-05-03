package com.marat.hvatit.playlistmaker2.domain.impl

import com.marat.hvatit.playlistmaker2.domain.api.interactors.SaveTrackInteractor
import com.marat.hvatit.playlistmaker2.domain.api.repository.SaveTrackRepository
import com.marat.hvatit.playlistmaker2.domain.models.Track

class SaveTrackInteractorImpl(val saveTrackRepositoryImpl: SaveTrackRepository) : SaveTrackInteractor{
    override val tracks: List<Track>
        get() = saveTrackRepositoryImpl.tracks

    override fun addSaveSongs(item: Track) {
        if (saveTrackRepositoryImpl.searchId(item)) {
            saveTrackRepositoryImpl.remove(item)
        }
        saveTrackRepositoryImpl.pushElement(item)
    }

    override fun getSaveTracks(): List<Track> {
        return saveTrackRepositoryImpl.tracks
    }

    override fun saveTracksToCache() {
        this.saveTrackRepositoryImpl.onDestroyStack()
    }

    override fun clearSaveStack() {
        saveTrackRepositoryImpl.clear()
    }

}