package com.marat.hvatit.playlistmaker2.creator

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.marat.hvatit.playlistmaker2.data.db.AppDatabase
import com.marat.hvatit.playlistmaker2.di.dataModule
import com.marat.hvatit.playlistmaker2.di.interactorModule
import com.marat.hvatit.playlistmaker2.di.repositoryModule
import com.marat.hvatit.playlistmaker2.di.utilModule
import com.marat.hvatit.playlistmaker2.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class PlaylistMakerApp : Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@PlaylistMakerApp)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule, utilModule)
        }
    }

    companion object {
        private var instance: PlaylistMakerApp? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}