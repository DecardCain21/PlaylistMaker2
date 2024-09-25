package com.marat.hvatit.playlistmaker2.presentation.medialibrary.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.databinding.PlaylistsFragmentBinding
import com.marat.hvatit.playlistmaker2.presentation.adapters.ItemPlaylist
import com.marat.hvatit.playlistmaker2.presentation.adapters.PlaylistAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistsFragment : Fragment() {
    companion object {
        private const val TESTSTRING = "Android1"

        fun newInstance(str: String) = PlaylistsFragment().apply {
            arguments = Bundle().apply {
                putString(TESTSTRING, str)
            }
        }
    }

    private val featuredPlaylistsViewModel: PlaylistsViewModel by viewModel {
        parametersOf(requireArguments().getString(TESTSTRING))
    }

    private val viewModel: PlaylistsViewModel by viewModel<PlaylistsViewModel>()
    private val playlistAdapter = PlaylistAdapter()

    private var _binding: PlaylistsFragmentBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PlaylistsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonNewbplaylist.setOnClickListener {
            findNavController().navigate(R.id.action_medialibraryFragment_to_newPlaylistFragment2)
        }
        binding.llPlaceholder.isVisible = false
        binding.playlists.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.playlists.adapter = playlistAdapter
        viewModel.getPlaylistsState().observe(viewLifecycleOwner) { state ->
            statePlaylists(state)
        }
        //viewModel.savePlaylist(testList[0])
        //viewModel.savePlaylist(testList[1])
        //viewModel.deletePlaylist(Playlist("2","","","",""))
        viewModel.getPlaylists()

    }

    private fun statePlaylists(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Data -> playlistAdapter.update(state.data,ItemPlaylist.TYPE_VERTICAL)
            PlaylistsState.EmptyState -> playlistAdapter.update(emptyList(),ItemPlaylist.TYPE_VERTICAL)
        }
        playlistAdapter.notifyDataSetChanged()
    }
}