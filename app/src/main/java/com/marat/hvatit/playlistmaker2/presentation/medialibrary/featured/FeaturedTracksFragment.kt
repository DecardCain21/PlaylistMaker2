package com.marat.hvatit.playlistmaker2.presentation.medialibrary.featured

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.data.JsonParserImpl
import com.marat.hvatit.playlistmaker2.databinding.FeaturedtracksFragmentBinding
import com.marat.hvatit.playlistmaker2.presentation.audioplayer.AudioplayerActivity
import com.marat.hvatit.playlistmaker2.presentation.adapters.TrackListAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeaturedTracksFragment : Fragment() {
    companion object {
        private const val TESTSTRING = "Android"
        private const val CLICK_DEBOUNCE_DELAY = 1000L

        fun newInstance(str: String) = FeaturedTracksFragment().apply {
            arguments = Bundle().apply {
                putString(TESTSTRING, str)
            }
        }
    }
    private val gsonParser: JsonParserImpl by inject()
    private var isClickAllowed = true

    private val viewModel by viewModel<FeaturedViewModel>()

    private var _binding: FeaturedtracksFragmentBinding? = null
    private val trackListAdapter = TrackListAdapter()
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FeaturedtracksFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.songlist.layoutManager = LinearLayoutManager(requireContext())
        binding.songlist.adapter = trackListAdapter
        binding.fragmentFeaturedPlaceholder
        binding.fragmentFeaturedPlaceholdertext
        viewModel.getFeaturedState().observe(viewLifecycleOwner) { state ->
            stateFeatured(state)
        }
        viewModel.getFeaturedTracks()
        trackListAdapter.saveTrackListener = TrackListAdapter.SaveTrackListener {
            if (clickDebounce()){
                AudioplayerActivity.getIntent(requireContext(),this.getString(R.string.android))
                    .apply {
                        putExtra("Track",gsonParser.objectToJson(it))
                        startActivity(this)
                    }
            }
        }
    }

    private fun stateFeatured(state: FeaturedState) {
        when (state) {
            is FeaturedState.Data -> {
                trackListAdapter.update(state.data)
                binding.fragmentFeaturedPlaceholder.isVisible = false
                binding.fragmentFeaturedPlaceholdertext.isVisible = false

            }

            FeaturedState.EmptyState -> {
                trackListAdapter.update(emptyList())
                binding.fragmentFeaturedPlaceholder.isVisible = true
                binding.fragmentFeaturedPlaceholdertext.isVisible = true

            }
        }
        trackListAdapter.notifyDataSetChanged()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFeaturedTracks()
    }

}