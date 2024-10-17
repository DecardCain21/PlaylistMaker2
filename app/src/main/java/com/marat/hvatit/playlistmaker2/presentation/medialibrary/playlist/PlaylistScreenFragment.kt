package com.marat.hvatit.playlistmaker2.presentation.medialibrary.playlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.marat.hvatit.playlistmaker2.data.JsonParserImpl
import com.marat.hvatit.playlistmaker2.databinding.PlaylistFragmentBinding
import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import com.marat.hvatit.playlistmaker2.presentation.adapters.TrackListAdapter
import com.marat.hvatit.playlistmaker2.presentation.utils.GlideHelper
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistScreenFragment : Fragment() {
    private var _binding: PlaylistFragmentBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var result: Playlist
    private val gsonParser: JsonParserImpl by inject()
    private val glide: GlideHelper by inject()
    private val viewModel by viewModel<PlaylistScreenViewModel>()
    private val trackListAdapter = TrackListAdapter()

    companion object {
        const val ARGS_KEY = "PLAYLIST"

        fun createArgs(playlist: String): Bundle {
            return bundleOf(ARGS_KEY to playlist)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments?.getString(ARGS_KEY) ?: "error"
        result = gsonParser.jsonToObject(args, Playlist::class.java)
        setTextContent(result)
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = trackListAdapter
        viewModel.getTracksState().observe(viewLifecycleOwner) {
            setTrackListState(it)
        }
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
        viewModel.getTracksById(result.playlistId)
    }

    private fun setTrackListState(tracksState: PlaylistTracksState) {
        when (tracksState) {
            is PlaylistTracksState.Data -> {
                trackListAdapter.update(tracksState.data)
                Log.e("getTracks", "tracksState.data:$tracksState.data")
            }

            PlaylistTracksState.EmptyState -> {
                trackListAdapter.update(emptyList())
            }
        }
        trackListAdapter.notifyDataSetChanged()

    }

    private fun setTextContent(playlist: Playlist) {
        Log.e("playlistContent", "$playlist")
        glide.setImageDb(
            context = requireContext(),
            covername = playlist.playlistCoverUrl,
            imageView = binding.playlistCover,
            GlideHelper.DEFAULT_CORNER_RADIUS
        )
        binding.playlistName.text = playlist.playlistName
        binding.playlistCount.text = playlist.playlistSize
        binding.playlistDescription.text = playlist.playlistDescription
        binding.playlistTime.text = "0"
        binding.playlistCount.text = "0"

    }

}