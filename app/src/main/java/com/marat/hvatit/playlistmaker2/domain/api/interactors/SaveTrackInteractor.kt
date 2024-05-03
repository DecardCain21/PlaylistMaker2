package com.marat.hvatit.playlistmaker2.domain.api.interactors

import com.marat.hvatit.playlistmaker2.domain.models.Track

interface SaveTrackInteractor {

    val tracks: List<Track>
    fun addSaveSongs(item: Track)

    fun getSaveTracks() : List<Track>

    fun saveTracksToCache()

    fun clearSaveStack()

}