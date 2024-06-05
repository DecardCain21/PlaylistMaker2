package com.marat.hvatit.playlistmaker2.presentation.medialibrary

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.marat.hvatit.playlistmaker2.databinding.ActivityMedialibraryBinding
import org.koin.core.component.getScopeName

class MedialibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedialibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedialibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = MedialibraryViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout,binding.viewPager){
            tab,position->
            when(position){
                0->tab.text = "Избранные треки"
                1->tab.text = "Плейлисты"
            }
        }
        tabMediator.attach()
        Log.e("viewPagerMedialib","${tabMediator.getScopeName()}")

    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}