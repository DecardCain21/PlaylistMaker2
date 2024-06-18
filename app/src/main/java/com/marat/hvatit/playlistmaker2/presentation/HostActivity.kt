package com.marat.hvatit.playlistmaker2.presentation

/*import android.os.Build.VERSION_CODES.R*/
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.presentation.medialibrary.MedialibraryFragment

class HostActivity : AppCompatActivity(R.layout.activity_host) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if(savedInstanceState == null){
            /*supportFragmentManager.commit { add(R.id.fragment_container_host,SearchFragment()) }*/
            /*supportFragmentManager.commit { add(R.id.fragment_container_host,SettingsFragment()) }*/
            supportFragmentManager.commit { add(R.id.fragment_container_host,MedialibraryFragment()) }

        }

    }
}