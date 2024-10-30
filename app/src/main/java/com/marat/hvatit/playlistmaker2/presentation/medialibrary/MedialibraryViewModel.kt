package com.marat.hvatit.playlistmaker2.presentation.medialibrary

import androidx.lifecycle.ViewModel
import com.marat.hvatit.playlistmaker2.domain.api.usecase.application.GetThemeUseCase


class MedialibraryViewModel(private val getThemeUseCase: GetThemeUseCase) : ViewModel() {

    fun isDarkMode(): Boolean {
        return getThemeUseCase.execute()
    }
}