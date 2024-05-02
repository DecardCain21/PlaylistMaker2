package com.marat.hvatit.playlistmaker2.domain.models

import com.marat.hvatit.playlistmaker2.data.dataSource.HistoryStorage
import java.util.Stack

class SaveTrackRepository<T>(private val maxSize: Int, private val historyStorage: HistoryStorage) {

    // внутреннее состояние треков
    private val _tracks: Stack<Track> = Stack()

    val tracks: List<Track>
        get() = _tracks

    init {
        _tracks.addAll(historyStorage.trackList)
    }

    fun remove(item: Track) {
        _tracks.remove(item)
    }

    fun clear() {
        _tracks.clear()
    }

    fun pushElement(item: Track) {
        if (_tracks.size >= maxSize) {
            _tracks.removeAt(_tracks.size - 1)
        }
        _tracks.add(0, item)
        saveItemsToCache(_tracks)
    }


    fun searchId(item: Track): Boolean {
        for (i in _tracks) {
            if (i.trackId == item.trackId) {
                return true
            }
        }
        return false
    }

    fun onDestroyStack() {
        saveItemsToCache(_tracks)
    }

    fun getItemsFromCache(): List<Track>? {
        return historyStorage.trackList
    }

    private fun saveItemsToCache(newItems: List<Track>) {
        historyStorage.trackList = newItems
    }

}