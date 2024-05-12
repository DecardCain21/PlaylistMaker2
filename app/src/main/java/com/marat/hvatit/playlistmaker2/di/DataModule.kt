package com.marat.hvatit.playlistmaker2.di

import com.google.gson.Gson
import com.marat.hvatit.playlistmaker2.data.NetworkClient
import com.marat.hvatit.playlistmaker2.data.TrackRepositoryImpl
import com.marat.hvatit.playlistmaker2.data.network.AppleMusicApiService
import com.marat.hvatit.playlistmaker2.data.network.RetrofitNetworkClient
import com.marat.hvatit.playlistmaker2.domain.api.interactors.TrackInteractor
import com.marat.hvatit.playlistmaker2.domain.api.repository.TrackRepository
import com.marat.hvatit.playlistmaker2.domain.impl.TrackInteractorImpl
import org.koin.android.ext.koin.androidContext
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

    factory<TrackRepository> { TrackRepositoryImpl(get()) }

    factory<TrackInteractor> { TrackInteractorImpl(get()) }


}