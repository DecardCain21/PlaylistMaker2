package com.marat.hvatit.playlistmaker2.di

import com.marat.hvatit.playlistmaker2.domain.api.interactors.AudioPlayerInteractor
import com.marat.hvatit.playlistmaker2.domain.api.interactors.SaveTrackInteractor
import com.marat.hvatit.playlistmaker2.domain.api.interactors.SettingsInteractor
import com.marat.hvatit.playlistmaker2.domain.api.interactors.TrackInteractor
import com.marat.hvatit.playlistmaker2.domain.api.usecase.AddCrossRefUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.AddPlaylistTrackUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.DeletePlaylistCrossRefUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.DeletePlaylistTrackNoRefUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.DeletePlaylistUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.GetPlaylistByIdUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.GetPlaylistTracksUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.GetPlaylistsUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.application.GetThemeUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.UpdatePlaylistUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.playlists.AddPlaylistUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.tracks.AddTrackUseCase
import com.marat.hvatit.playlistmaker2.domain.impl.AudioPlayerInteractorImpl
import com.marat.hvatit.playlistmaker2.domain.api.usecase.tracks.DeleteTrackUseCase
import com.marat.hvatit.playlistmaker2.domain.api.usecase.tracks.GetFavoriteTracksUseCase
import com.marat.hvatit.playlistmaker2.domain.impl.SaveTrackInteractorImpl
import com.marat.hvatit.playlistmaker2.domain.impl.SettingsInteractorImpl
import com.marat.hvatit.playlistmaker2.domain.impl.TrackInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<TrackInteractor> {
        TrackInteractorImpl(get())
    }

    single<SaveTrackInteractor> {
        SaveTrackInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory<AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(get())
    }

    single<GetPlaylistsUseCase> {
        GetPlaylistsUseCase(get())
    }

    single {
        AddCrossRefUseCase(get())
    }
    single {
        GetPlaylistTracksUseCase(get())
    }

    single {
        AddPlaylistTrackUseCase(get())
    }

    single {
        UpdatePlaylistUseCase(get())
    }

    single {
        DeletePlaylistCrossRefUseCase(get())
    }

    single {
        DeletePlaylistTrackNoRefUseCase(get())
    }

    single {
        DeletePlaylistUseCase(get())
    }

    single {
        GetPlaylistByIdUseCase(get())
    }

    single {
        GetFavoriteTracksUseCase(get())
    }

    single {
        AddTrackUseCase(get())
    }

    single {
        DeleteTrackUseCase(get())
    }

    single {
        GetThemeUseCase(get())
    }

    single {
        AddPlaylistUseCase(get())
    }


}