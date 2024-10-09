package com.marat.hvatit.playlistmaker2.presentation

/*import android.os.Build.VERSION_CODES.R*/
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.marat.hvatit.playlistmaker2.R

class HostActivity : AppCompatActivity(R.layout.activity_host) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_host) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { navController, destination, arguments ->
            if (destination.id == R.id.fragmentAgreement || destination.id == R.id.newPlaylistFragment || destination.id == R.id.audioPlayerFragment) {
                bottomNavigationView.visibility = View.GONE
            } else {
                bottomNavigationView.visibility = View.VISIBLE
            }
        }

    }
}