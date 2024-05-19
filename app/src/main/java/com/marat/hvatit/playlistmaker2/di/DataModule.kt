package com.marat.hvatit.playlistmaker2.di

import android.content.Context
import com.google.gson.Gson
import com.marat.hvatit.playlistmaker2.data.JsonParserImpl
import com.marat.hvatit.playlistmaker2.data.NetworkClient
import com.marat.hvatit.playlistmaker2.data.dataSource.HistoryStorage
import com.marat.hvatit.playlistmaker2.data.dataSource.HistoryStorageImpl
import com.marat.hvatit.playlistmaker2.data.network.AppleMusicApiService
import com.marat.hvatit.playlistmaker2.data.network.RetrofitNetworkClient
import com.marat.hvatit.playlistmaker2.domain.api.JsonParser
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val KEY_CART = "cart"
private const val APPLE_BASE_URL = "https://itunes.apple.com"

val dataModule = module {

    single<AppleMusicApiService> {
        Retrofit.Builder()
            .baseUrl(APPLE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AppleMusicApiService::class.java)
    }

    factory { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }

    single<HistoryStorage>{
        HistoryStorageImpl(androidContext(),get(),get())
    }

    single { androidContext().getSharedPreferences(KEY_CART, Context.MODE_PRIVATE) }

    single{
        JsonParserImpl(get())
    } bind JsonParser::class
}