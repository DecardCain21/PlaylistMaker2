package com.marat.hvatit.playlistmaker2.domain.api.interactors

interface SettingsInteractor {

    fun getPrefTheme(): Boolean

    fun editPrefTheme(value: Boolean)

}