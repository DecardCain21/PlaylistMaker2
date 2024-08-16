package com.marat.hvatit.playlistmaker2.di

import com.marat.hvatit.playlistmaker2.data.FavoritesRepositoryImpl
import com.marat.hvatit.playlistmaker2.data.SaveTrackRepositoryImpl
import com.marat.hvatit.playlistmaker2.data.TrackRepositoryImpl
import com.marat.hvatit.playlistmaker2.data.db.converters.TrackDbConvertor
import com.marat.hvatit.playlistmaker2.domain.api.repository.SaveTrackRepository
import com.marat.hvatit.playlistmaker2.domain.api.repository.TrackRepository
import com.marat.hvatit.playlistmaker2.domain.db.FavoritesRepository
import org.koin.dsl.module

private const val STORY_TRACK_SIZE = 10

val repositoryModule = module {

    single<TrackRepository> {
        TrackRepositoryImpl(get())
    }

    single<SaveTrackRepository> {
        SaveTrackRepositoryImpl(STORY_TRACK_SIZE, get())
    }

    factory { TrackDbConvertor() }

    single<FavoritesRepository> { FavoritesRepositoryImpl(get(), get()) }
}