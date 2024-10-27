package com.marat.hvatit.playlistmaker2.presentation.medialibrary

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.marat.hvatit.playlistmaker2.presentation.medialibrary.featured.FeaturedTracksFragment
import com.marat.hvatit.playlistmaker2.presentation.medialibrary.playlists.PlaylistsFragment


class MedialibraryViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FeaturedTracksFragment.newInstance("str")
            1 -> PlaylistsFragment.newInstance("Android")
            else -> PlaylistsFragment.newInstance("str")
        }
    }
}