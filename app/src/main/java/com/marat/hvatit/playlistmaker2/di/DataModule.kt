package com.marat.hvatit.playlistmaker2.di

import com.google.gson.Gson
import com.marat.hvatit.playlistmaker2.creator.Creator
import com.marat.hvatit.playlistmaker2.data.NetworkClient
import com.marat.hvatit.playlistmaker2.data.dataSource.HistoryStorage
import com.marat.hvatit.playlistmaker2.data.dataSource.HistoryStorageImpl
import com.marat.hvatit.playlistmaker2.data.network.AppleMusicApiService
import com.marat.hvatit.playlistmaker2.data.network.RetrofitNetworkClient
import com.marat.hvatit.playlistmaker2.domain.api.JsonParser
import com.marat.hvatit.playlistmaker2.domain.impl.JsonParserImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val dataModule = module {

    single<AppleMusicApiService> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AppleMusicApiService::class.java)
    }



    factory { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }

    single<HistoryStorage>{
        HistoryStorageImpl(androidContext(),Creator.provideSharedPref(),get())
    }

    //single <JsonParser>{JsonParserImpl(get())  }

    single{
        JsonParserImpl(get())
    } bind JsonParser::class
}