package com.marat.hvatit.playlistmaker2.di

import com.marat.hvatit.playlistmaker2.common.GlideHelperImpl
import com.marat.hvatit.playlistmaker2.presentation.utils.GlideHelper
import org.koin.dsl.module

val utilModule = module {

   single<GlideHelper>{
       GlideHelperImpl()
   }

}