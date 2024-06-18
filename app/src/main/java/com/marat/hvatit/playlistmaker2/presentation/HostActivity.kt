package com.marat.hvatit.playlistmaker2.presentation

/*import android.os.Build.VERSION_CODES.R*/
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.marat.hvatit.playlistmaker2.R

class HostActivity : AppCompatActivity(R.layout.activity_host) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        /*if(savedInstanceState == null){
           *//* supportFragmentManager.commit { add(R.id.fragment_container_host, SearchFragment()) }
            supportFragmentManager.commit { add(R.id.fragment_container_host, SettingsFragment()) }*//*
            supportFragmentManager.commit { add(R.id.fragment_container_host,
                MedialibraryFragment()
            ) }

        }*/
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_host) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)


    }
}