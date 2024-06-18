package com.marat.hvatit.playlistmaker2.presentation.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.presentation.medialibrary.MedialibraryActivity
import com.marat.hvatit.playlistmaker2.presentation.search.SearchFragment
import com.marat.hvatit.playlistmaker2.presentation.settings.SettingsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModel<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setThemePref()
        initButtonSearch()
        initButtonMediaLib()
        initButtonSettings()

    }

    private fun initButtonSettings() {
        val buttonSettings = findViewById<Button>(R.id.button_bigThree)
        buttonSettings.setOnClickListener {
            SettingsActivity.getIntent(this@MainActivity, this.getString(R.string.android)).apply {
                startActivity(this)
            }
        }
    }

    private fun initButtonMediaLib() {
        val buttonMedialib = findViewById<Button>(R.id.button_bigTwo)
        buttonMedialib.setOnClickListener {
            val medialibIntent = Intent(this, MedialibraryActivity::class.java)
            startActivity(medialibIntent)
        }
    }

    private fun initButtonSearch() {
        val buttonSearch = findViewById<Button>(R.id.button_bigOne)
        buttonSearch.setOnClickListener {
            SearchFragment.getIntent(this@MainActivity, this.getString(R.string.android)).apply {
                startActivity(this)
            }
        }
    }

    private fun setThemePref() {
        var flag: Boolean = viewModel.isDarkMode()
        val mode = if (flag) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}