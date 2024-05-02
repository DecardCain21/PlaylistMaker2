package com.marat.hvatit.playlistmaker2.domain.impl

import com.marat.hvatit.playlistmaker2.data.dataSource.HistoryStorage
import com.marat.hvatit.playlistmaker2.domain.api.interactors.MainInteractor

class MainInteractorImpl(private val historyStorage: HistoryStorage) : MainInteractor {
    override fun getPrefTheme(): Boolean {
        return historyStorage.getUserTheme()
    }
}