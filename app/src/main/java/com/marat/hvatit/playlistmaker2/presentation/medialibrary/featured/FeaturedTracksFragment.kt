package com.marat.hvatit.playlistmaker2.presentation.medialibrary.featured

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.marat.hvatit.playlistmaker2.databinding.FeaturedtracksFragmentBinding
import com.marat.hvatit.playlistmaker2.presentation.search.TrackListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeaturedTracksFragment : Fragment() {
    companion object {
        private const val TESTSTRING = "Android"

        fun newInstance(str: String) = FeaturedTracksFragment().apply {
            arguments = Bundle().apply {
                putString(TESTSTRING, str)
            }
        }
    }

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
        //trackListAdapter.update(viewModel.getFeaturedTracks())
        binding.fragmentFeaturedPlaceholder
        binding.fragmentFeaturedPlaceholdertext
        viewModel.getFeaturedState().observe(viewLifecycleOwner) { state ->
            stateFeatured(state)
        }
        viewModel.getFeaturedTracks()
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

}