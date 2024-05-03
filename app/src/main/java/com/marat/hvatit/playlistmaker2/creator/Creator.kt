package com.marat.hvatit.playlistmaker2.creator

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.marat.hvatit.playlistmaker2.common.GlideHelperImpl
import com.marat.hvatit.playlistmaker2.data.SaveTrackRepositoryImpl
import com.marat.hvatit.playlistmaker2.data.TrackRepositoryImpl
import com.marat.hvatit.playlistmaker2.data.dataSource.HistoryStorage
import com.marat.hvatit.playlistmaker2.data.dataSource.HistoryStorageImpl
import com.marat.hvatit.playlistmaker2.data.dto.JsonParserImpl
import com.marat.hvatit.playlistmaker2.data.network.RetrofitNetworkClient
import com.marat.hvatit.playlistmaker2.domain.api.AudioPlayerCallback
import com.marat.hvatit.playlistmaker2.domain.api.JsonParser
import com.marat.hvatit.playlistmaker2.domain.api.interactors.AudioPlayerInteractor
import com.marat.hvatit.playlistmaker2.domain.api.interactors.MainInteractor
import com.marat.hvatit.playlistmaker2.domain.api.interactors.SaveTrackInteractor
import com.marat.hvatit.playlistmaker2.domain.api.interactors.SettingsInteractor
import com.marat.hvatit.playlistmaker2.domain.api.interactors.TrackInteractor
import com.marat.hvatit.playlistmaker2.domain.api.repository.SaveTrackRepository
import com.marat.hvatit.playlistmaker2.domain.api.repository.TrackRepository
import com.marat.hvatit.playlistmaker2.domain.impl.AudioPlayerInteractorImpl
import com.marat.hvatit.playlistmaker2.domain.impl.MainInteractorImpl
import com.marat.hvatit.playlistmaker2.domain.impl.SaveTrackInteractorImpl
import com.marat.hvatit.playlistmaker2.domain.impl.SettingsInteractorImpl
import com.marat.hvatit.playlistmaker2.domain.impl.TrackInteractorImpl
import com.marat.hvatit.playlistmaker2.presentation.audioplayer.AudioPlayerControllerImpl
import com.marat.hvatit.playlistmaker2.presentation.settings.IntentNavigator
import com.marat.hvatit.playlistmaker2.presentation.settings.IntentNavigatorImpl
import com.marat.hvatit.playlistmaker2.presentation.utils.GlideHelper

object Creator {

    private const val KEY_CART = "cart"

    private const val APPLE_BASE_URL = "https://itunes.apple.com"

    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(
            RetrofitNetworkClient(
                PlaylistMakerApp.applicationContext(),
                APPLE_BASE_URL
            )
        )
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

    fun provideSaveTrackInteractor() : SaveTrackInteractor{
        return SaveTrackInteractorImpl(provideSaveTrackRepository(10))
    }

    fun provideAudioPlayer(
        priviewUrl: String,
        callback: AudioPlayerCallback
    ): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(AudioPlayerControllerImpl(priviewUrl, callback))
    }

    fun provideJsonParser(): JsonParser {
        return JsonParserImpl(provideGson())
    }

    fun provideGlideHelper(): GlideHelper {
        return GlideHelperImpl()
    }

    fun provideSaveTrackRepository(size: Int): SaveTrackRepository {
        return SaveTrackRepositoryImpl(size, provideHistoryTracks())
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(provideHistoryTracks())
    }

    fun provideMainInteractor(): MainInteractor {
        return MainInteractorImpl(provideHistoryTracks())
    }

    fun provideIntentNavigator(context: Context): IntentNavigator {
        return IntentNavigatorImpl(context)
    }

    private fun provideHistoryTracks(): HistoryStorage {
        return HistoryStorageImpl(
            PlaylistMakerApp.applicationContext(), provideSharedPref(),
            provideGson()
        )
    }

    private fun provideSharedPref(): SharedPreferences {
        return PlaylistMakerApp.applicationContext()
            .getSharedPreferences(KEY_CART, Context.MODE_PRIVATE)
    }

    private fun provideGson(): Gson {
        return Gson()
    }

}