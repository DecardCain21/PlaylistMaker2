package com.marat.hvatit.playlistmaker2.di

import com.marat.hvatit.playlistmaker2.data.FavoritesRepositoryImpl
import com.marat.hvatit.playlistmaker2.data.PlaylistsRepositoryImpl
import com.marat.hvatit.playlistmaker2.data.SaveTrackRepositoryImpl
import com.marat.hvatit.playlistmaker2.data.TrackRepositoryImpl
import com.marat.hvatit.playlistmaker2.data.db.converters.PlaylistDbConvertor
import com.marat.hvatit.playlistmaker2.data.db.converters.TrackDbConvertor
import com.marat.hvatit.playlistmaker2.domain.api.repository.SaveTrackRepository
import com.marat.hvatit.playlistmaker2.domain.api.repository.TrackRepository
import com.marat.hvatit.playlistmaker2.domain.favorites.FavoritesRepository
import com.marat.hvatit.playlistmaker2.domain.playlists.PlaylistsRepository
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

    factory { PlaylistDbConvertor() }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }

    single<PlaylistsRepository> {
        PlaylistsRepositoryImpl(get(), get())
    }
}