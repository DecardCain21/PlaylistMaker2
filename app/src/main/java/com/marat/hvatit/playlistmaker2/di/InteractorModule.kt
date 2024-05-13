package com.marat.hvatit.playlistmaker2.di

import com.marat.hvatit.playlistmaker2.domain.api.interactors.SaveTrackInteractor
import com.marat.hvatit.playlistmaker2.domain.api.interactors.TrackInteractor
import com.marat.hvatit.playlistmaker2.domain.impl.SaveTrackInteractorImpl
import com.marat.hvatit.playlistmaker2.domain.impl.TrackInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<TrackInteractor> {
        TrackInteractorImpl(get())
    }

    single<SaveTrackInteractor> {
        SaveTrackInteractorImpl(get())
    }

}