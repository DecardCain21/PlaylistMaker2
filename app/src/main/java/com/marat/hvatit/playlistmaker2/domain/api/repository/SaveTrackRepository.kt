package com.marat.hvatit.playlistmaker2.domain.api.repository

import com.marat.hvatit.playlistmaker2.domain.models.Track

interface SaveTrackRepository {

    val tracks: List<Track>

    fun remove(item: Track)

    fun searchId(item: Track): Boolean

    fun pushElement(item: Track)

    fun onDestroyStack()

    fun clear()

}