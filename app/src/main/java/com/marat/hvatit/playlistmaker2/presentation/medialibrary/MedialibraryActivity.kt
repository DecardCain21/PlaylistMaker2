package com.marat.hvatit.playlistmaker2.presentation.medialibrary

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.databinding.ActivityMedialibraryBinding
import org.koin.core.component.getScopeName

class MedialibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedialibraryBinding
    private lateinit var tabMediator: TabLayoutMediator
    private lateinit var buttonBack : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedialibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = MedialibraryViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout,binding.viewPager){
            tab,position->
            when(position){
                0->tab.text = getString(R.string.featured_trucks)
                1->tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
        Log.e("viewPagerMedialib","${tabMediator.getScopeName()}")
        binding.back.setOnClickListener { onBackPressed() }


    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}