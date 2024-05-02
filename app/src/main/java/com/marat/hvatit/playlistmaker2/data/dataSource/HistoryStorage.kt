package com.marat.hvatit.playlistmaker2.data.dataSource

import com.marat.hvatit.playlistmaker2.domain.models.Track

interface HistoryStorage {

    var trackList: List<Track>

    fun editDefaultTheme(flag: Boolean)

    fun getUserTheme(): Boolean

}