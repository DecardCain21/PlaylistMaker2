package com.marat.hvatit.playlistmaker2.presentation.medialibrary.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.data.JsonParserImpl
import com.marat.hvatit.playlistmaker2.databinding.PlaylistFragmentBinding
import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import com.marat.hvatit.playlistmaker2.presentation.adapters.TrackListAdapter
import com.marat.hvatit.playlistmaker2.presentation.utils.GlideHelper
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Collections.emptyList
import java.util.Date
import java.util.Locale

class PlaylistScreenFragment : Fragment() {
    private var _binding: PlaylistFragmentBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var result: Playlist
    private val gsonParser: JsonParserImpl by inject()
    private val glide: GlideHelper by inject()
    private val viewModel by viewModel<PlaylistScreenViewModel>()
    private val trackListAdapter = TrackListAdapter()
    private lateinit var confirmDialog: MaterialAlertDialogBuilder
    private val simpleDateFormat: SimpleDateFormat =
        SimpleDateFormat("mm", Locale.getDefault())

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
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        val bottomSheetBehaviorText = BottomSheetBehavior.from(binding.bottomSheetContainerTwo).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        //может убрать углы?
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
        bottomSheetBehaviorText.addBottomSheetCallback(object :BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

        })
        binding.playlistPointMenu.setOnClickListener {
            Toast.makeText(requireContext(), "MenuPoint!", Toast.LENGTH_LONG).show()
            bottomSheetBehaviorText.state = BottomSheetBehavior.STATE_EXPANDED
        }
        confirmDialog = MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setTitle("Хотите удалить трек?")
            .setNegativeButton("Нет") { _, _ ->

            }
        viewModel.getTracksState().observe(viewLifecycleOwner) {
            setTrackListState(it)
        }
        viewModel.getTracksVolume().observe(viewLifecycleOwner) {
            setTracksVolume(it)
        }
        viewModel.getTracksSize().observe(viewLifecycleOwner) {
            setTracksSize(it)
        }
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
        viewModel.getTracksById(result.playlistId)
        trackListAdapter.longClickTrackListener = TrackListAdapter.LongClickTrackListener {
            confirmDialog.setPositiveButton("Да") { _, _ ->
                deleteTrack(playlist = result, trackId = it.trackId)
                viewModel.getTracksById(result.playlistId)
            }
            confirmDialog.show()
            trackListAdapter.notifyDataSetChanged()
        }
    }

    private fun setTracksVolume(volume: Int?) {
        var result: Date? = volume?.let { Date(it.toLong()) }
        binding.playlistTime.text = simpleDateFormat.format(result) + " минут"
    }

    private fun setTracksSize(newSize: Int) {
        binding.playlistCount.text = newSize.toString()
    }

    private fun deleteTrack(playlist: Playlist, trackId: String) {
        viewModel.deletePlaylistCrossReference(playlist, trackId)
        viewModel.deletePlaylistTrackNoReferences(trackId)
        trackListAdapter.notifyDataSetChanged()
    }

    private fun setTrackListState(tracksState: PlaylistTracksState) {
        when (tracksState) {
            is PlaylistTracksState.Data -> {
                trackListAdapter.update(tracksState.data)
            }

            PlaylistTracksState.EmptyState -> {
                trackListAdapter.update(emptyList())
            }
        }
        trackListAdapter.notifyDataSetChanged()

    }

    private fun setTextContent(playlist: Playlist) {
        glide.setImageDb(
            context = requireContext(),
            covername = playlist.playlistCoverUrl,
            imageView = binding.playlistCover,
            GlideHelper.DEFAULT_CORNER_RADIUS
        )
        binding.playlistName.text = playlist.playlistName
        binding.playlistDescription.text = playlist.playlistDescription
    }

}