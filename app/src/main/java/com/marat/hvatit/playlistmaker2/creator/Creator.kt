package com.marat.hvatit.playlistmaker2.creator

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.marat.hvatit.playlistmaker2.common.GlideHelperImpl
import com.marat.hvatit.playlistmaker2.data.JsonParserImpl
import com.marat.hvatit.playlistmaker2.data.dataSource.HistoryStorage
import com.marat.hvatit.playlistmaker2.data.dataSource.HistoryStorageImpl
import com.marat.hvatit.playlistmaker2.domain.api.JsonParser
import com.marat.hvatit.playlistmaker2.domain.api.interactors.MainInteractor
import com.marat.hvatit.playlistmaker2.domain.impl.MainInteractorImpl
import com.marat.hvatit.playlistmaker2.presentation.utils.GlideHelper

object Creator {

    private const val KEY_CART = "cart"

    private const val APPLE_BASE_URL = "https://itunes.apple.com"

    private const val STORY_TRACK_SIZE = 10

    fun provideJsonParser(): JsonParser {
        return JsonParserImpl(provideGson())
    }

    fun provideGlideHelper(): GlideHelper {
        return GlideHelperImpl()
    }


    fun provideMainInteractor(): MainInteractor {
        return MainInteractorImpl(provideHistoryTracks())
    }

    private fun provideHistoryTracks(): HistoryStorage {
        return HistoryStorageImpl(
            PlaylistMakerApp.applicationContext(), provideSharedPref(),
            provideGson()
        )
    }

    fun provideSharedPref(): SharedPreferences {
        return PlaylistMakerApp.applicationContext()
            .getSharedPreferences(KEY_CART, Context.MODE_PRIVATE)
    }

    private fun provideGson(): Gson {
        return Gson()
    }

}