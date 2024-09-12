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
import com.marat.hvatit.playlistmaker2.domain.models.Playlist
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
        playlistAdapter.update(testHardcode())

    }

    fun testHardcode(): List<Playlist> {
        return listOf(
            Playlist(
                "1",
                "Котовасия",
                "2",
                "https://github.com/DecardCain21/SearchFilmsApp2/blob/master/covercat1.jpg",
                "Котовасия"
            ),
            Playlist(
                "2",
                "Котострофа",
                "3",
                "https://github.com/DecardCain21/SearchFilmsApp2/blob/master/covercat1.jpg",
                "Котострофа"
            )
        )
    }
}