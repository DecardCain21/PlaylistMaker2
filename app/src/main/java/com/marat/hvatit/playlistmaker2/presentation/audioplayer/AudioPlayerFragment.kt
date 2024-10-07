package com.marat.hvatit.playlistmaker2.presentation.audioplayer

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.data.JsonParserImpl
import com.marat.hvatit.playlistmaker2.databinding.FragmentAudioplayerBinding
import com.marat.hvatit.playlistmaker2.domain.models.Track
import com.marat.hvatit.playlistmaker2.presentation.adapters.ItemPlaylist
import com.marat.hvatit.playlistmaker2.presentation.adapters.PlaylistAdapter
import com.marat.hvatit.playlistmaker2.presentation.utils.GlideHelper
import com.marat.hvatit.playlistmaker2.presentation.utils.GlideHelper.Companion.addQuality
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Collections
import java.util.Locale

class AudioPlayerFragment : Fragment() {

    private lateinit var intent: Intent
    private val simpleDateFormat: SimpleDateFormat =
        SimpleDateFormat("mm:ss", Locale.getDefault())


    private var priviewUrl: String = ""


    private val gsonParser: JsonParserImpl by inject()
    private val glide: GlideHelper by inject()
    private val playlistAdapter = PlaylistAdapter()
    //private lateinit var recyclerView: RecyclerView


    private val viewModel: AudioViewModel by viewModel {
        parametersOf(priviewUrl)
    }


    private lateinit var binding: FragmentAudioplayerBinding

    companion object {

        const val ARGS_KEY = "TRACK"
        fun createArgs(track: String): Bundle {
            return bundleOf(ARGS_KEY to track)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAudioplayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments?.getString(ARGS_KEY) ?: "error"
        Log.e("AudioError", "args:$args")
        val result = gsonParser.jsonToObject(args, Track::class.java)
        Log.e("AudioError", "result:$args")
        //intent = getIntent(requireContext(), "Track")
        //val song = intent.getStringExtra("Track")
        //val result: Track = gsonParser.jsonToObject(song.toString(), Track::class.java)
        priviewUrl = result.previewUrl
        //initViews()
        setTextContent(result)

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        binding.bottomSheetParent.setBackgroundColor(Color.parseColor("#99000000"))
                        binding.bottomSheetParent.isVisible = true
                        viewModel.getPlaylists()

                    }

                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.bottomSheetParent.isVisible = false

                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.bottomSheetParent.isVisible = false

                    }

                    else -> {
                        // Остальные состояния не обрабатываем
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //backgroundBottomSheet.isVisible = slideOffset > 0
                //Log.e("SlideOffset", "${abs(slideOffset)}")
                //Неверно указываю смещение?
            }
        })
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.actplayerButtonplay.isEnabled = false

        viewModel.playbackControl()
        binding.actplayerButtonplay.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.getLoadingLiveData().observe(viewLifecycleOwner) { playerState ->
            uiControl(playerState)
        }

        viewModel.getFavoriteState().observe(viewLifecycleOwner) { it ->
            stateFavorite(it)
        }

        viewModel.defaultFavoriteState(result)
        binding.buttonFavorite.setOnClickListener {
            viewModel.setFavoriteState(result)
        }

        binding.buttonAddtoplaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.playlists.layoutManager = LinearLayoutManager(requireContext())
        binding.playlists.adapter = playlistAdapter
        viewModel.getPlaylistsState().observe(viewLifecycleOwner) {
            statePlaylists(it)
        }
        playlistAdapter.saveTrackToPlaylist = PlaylistAdapter.SaveToPlaylistListener {
            viewModel.addTrackToPlaylist(it.data, result,
                onSuccess = {
                    requireActivity().runOnUiThread {
                        Toast.makeText(
                            requireContext(),
                            "Add",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                onError = {
                    requireActivity().runOnUiThread {
                        Toast.makeText(
                            requireContext(),
                            "Not Added",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.buttonNewplaylist.setOnClickListener {
            findNavController().navigate(R.id.action_audioPlayerFragment_to_newPlaylistFragment)
        }
    }

    private fun setTextContent(song: Track) {
        glide.setImage(
            context = requireContext(),
            url = song.artworkUrl100.addQuality(),
            actplayerCover = binding.actplayerCover
        )
        binding.actplayerDurationvalue.text = simpleDateFormat.format(song.trackTimeMillis.toLong())
        binding.actplayerArtistName.text = song.artistName
        binding.actplayerTrackName.text = song.trackName
        binding.actplayerCountryvalue.text = song.country
        binding.actplayerGenrevalue.text = song.genre
        binding.actplayerYearvalue.text = song.year.substring(0, song.year.indexOf("-"))
        binding.actplayerAlbumvalue.text = song.album
        binding.actplayerTracktime.text = getString(R.string.default_timer)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaylists()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPausePlayer()
        binding.actplayerButtonplay.setBackgroundResource(R.drawable.button_play)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroyPlayer()
    }

    private fun stateFavorite(state: FavoriteState) {
        if (state is FavoriteState.IsFavorite && state.favorite) {
            binding.buttonFavorite.setBackgroundResource(R.drawable.favorite_button_active)
        } else {
            binding.buttonFavorite.setBackgroundResource(R.drawable.favorite_button_unactive)
        }
    }

    private fun uiControl(state: MediaPlayerState) {
        when (state) {
            is MediaPlayerState.Default -> {
                binding.actplayerButtonplay.setBackgroundResource(R.drawable.button_play)
            }

            is MediaPlayerState.Paused -> {
                binding.actplayerButtonplay.setBackgroundResource(R.drawable.button_play)
            }

            is MediaPlayerState.Playing -> {
                binding.actplayerButtonplay.setBackgroundResource(R.drawable.button_stop)
                binding.actplayerTracktime.text = state.currentTime
            }

            is MediaPlayerState.Prepared -> {
                binding.actplayerButtonplay.setBackgroundResource(R.drawable.button_play)
                binding.actplayerButtonplay.isEnabled = true
            }

            is MediaPlayerState.Completed -> {
                binding.actplayerButtonplay.setBackgroundResource(R.drawable.button_play)
                binding.actplayerTracktime.text = state.currentTime
            }

            is MediaPlayerState.Disconnected -> {
                Toast.makeText(requireContext(), "Disconnect_problem", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun statePlaylists(state: BottomPlaylistsState) {
        when (state) {
            is BottomPlaylistsState.Data -> playlistAdapter.update(
                state.data,
                ItemPlaylist.TYPE_HORIZONTAL
            )

            BottomPlaylistsState.EmptyState -> playlistAdapter.update(
                Collections.emptyList(),
                ItemPlaylist.TYPE_HORIZONTAL
            )
        }
        playlistAdapter.notifyDataSetChanged()
    }

}