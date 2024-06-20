package com.marat.hvatit.playlistmaker2.presentation.medialibrary

import androidx.lifecycle.ViewModel
import com.marat.hvatit.playlistmaker2.domain.api.interactors.MainInteractor

class MedialibraryViewModel(private val interactor: MainInteractor) : ViewModel() {

    fun isDarkMode(): Boolean {
        return interactor.getPrefTheme()
    }
}