package com.marat.hvatit.playlistmaker2.di

import com.marat.hvatit.playlistmaker2.presentation.search.SearchViewModel
import com.marat.hvatit.playlistmaker2.presentation.settings.IntentNavigator
import com.marat.hvatit.playlistmaker2.presentation.settings.IntentNavigatorImpl
import com.marat.hvatit.playlistmaker2.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single<IntentNavigator> {
        IntentNavigatorImpl(get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

}