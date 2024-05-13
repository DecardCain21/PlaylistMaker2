package com.marat.hvatit.playlistmaker2.di

import com.marat.hvatit.playlistmaker2.presentation.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get(),get())
    }

}