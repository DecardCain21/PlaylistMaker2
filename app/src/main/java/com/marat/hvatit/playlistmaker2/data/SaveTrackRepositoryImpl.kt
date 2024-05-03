package com.marat.hvatit.playlistmaker2.data

import com.marat.hvatit.playlistmaker2.data.dataSource.HistoryStorage
import com.marat.hvatit.playlistmaker2.domain.api.repository.SaveTrackRepository
import com.marat.hvatit.playlistmaker2.domain.models.Track
import java.util.Stack

class SaveTrackRepositoryImpl(
    private val maxSize: Int,
    private val historyStorage: HistoryStorage
) : SaveTrackRepository {

    // внутреннее состояние треков
    private val _tracks: Stack<Track> = Stack()

    override val tracks: List<Track>
        get() = _tracks

    init {
        _tracks.addAll(historyStorage.trackList)
    }

    override fun remove(item: Track) {
        _tracks.remove(item)
    }

    override fun clear() {
        _tracks.clear()
    }

    override fun pushElement(item: Track) {
        if (_tracks.size >= maxSize) {
            _tracks.removeAt(_tracks.size - 1)
        }
        _tracks.add(0, item)
        saveItemsToCache(_tracks)
    }


    override fun searchId(item: Track): Boolean {
        for (i in _tracks) {
            if (i.trackId == item.trackId) {
                return true
            }
        }
        return false
    }

    override fun onDestroyStack() {
        saveItemsToCache(_tracks)
    }

    private fun saveItemsToCache(newItems: List<Track>) {
        historyStorage.trackList = newItems
    }

}