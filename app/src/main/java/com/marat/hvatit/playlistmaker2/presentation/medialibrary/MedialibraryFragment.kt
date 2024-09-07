package com.marat.hvatit.playlistmaker2.presentation.medialibrary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.databinding.FragmentMedialibraryBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MedialibraryFragment : Fragment() {

    private val viewModel by viewModel<MedialibraryViewModel>()

    private var _binding: FragmentMedialibraryBinding? = null

    private var tabMediator: TabLayoutMediator? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMedialibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setThemePref()
        binding.viewPager.adapter = MedialibraryViewPagerAdapter(childFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.featured_trucks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator?.attach()
    }

    private fun setThemePref() {
        var flag: Boolean = viewModel.isDarkMode()
        val mode = if (flag) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator?.detach()
    }

}