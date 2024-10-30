package com.marat.hvatit.playlistmaker2.domain.api.usecase.application

import com.marat.hvatit.playlistmaker2.data.dataSource.HistoryStorage

class GetThemeUseCase(private val historyStorage: HistoryStorage) {
    fun execute(): Boolean {
        return historyStorage.getUserTheme()
    }
}