package com.marat.hvatit.playlistmaker2.presentation.medialibrary.newplaylist

import android.net.Uri
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.data.JsonParserImpl
import com.marat.hvatit.playlistmaker2.databinding.FragmentNewplaylistBinding
import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import com.marat.hvatit.playlistmaker2.presentation.medialibrary.playlist.PlaylistScreenFragment
import com.marat.hvatit.playlistmaker2.presentation.utils.GlideHelper
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : NewPlaylistFragment() {

    override val viewModel: EditPlaylistViewModel by viewModel()
    private val gsonParser: JsonParserImpl by inject()
    private val glide: GlideHelper by inject()
    private lateinit var result: Playlist
    override var saveEditTextName: String? = null
    override var saveEditTextDescription: String? = null
    override var coverUri: Uri? = null


    companion object {

        const val ARGS_KEY = "EDIT"
        fun createArgs(playlist: String): Bundle {
            return bundleOf(ARGS_KEY to playlist)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewplaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments?.getString(ARGS_KEY) ?: "error"
        result = gsonParser.jsonToObject(args, Playlist::class.java)
        glide.setImage(
            requireContext(),
            result.playlistCoverUrl,
            binding.cover,
            GlideHelper.VERTICAL_PLAYLIST_CORNER_RADIUS
        )
        //binding.cover.isEnabled = false
        binding.etName.setText(result.playlistName)
        binding.etDescription.setText(result.playlistDescription)
        binding.buttonCreate.setOnClickListener {
            coverUri?.let { it1 -> saveImage(it1) }
            playlistCover?.let {
                viewModel.createPlaylist(
                    playlistId = result.playlistId,
                    saveEditTextName = saveEditTextName.toString(),
                    saveEditTextDescription = saveEditTextDescription.toString(),
                    playlistSize = result.playlistSize,
                    covername = it,
                    onSuccess = {

                    }
                )
            }
            PlaylistScreenFragment.createArgs(
                gsonParser.objectToJson(
                    Playlist(
                        playlistId = result.playlistId,
                        playlistName = saveEditTextName.toString(),
                        playlistDescription = saveEditTextDescription.toString(),
                        playlistSize = result.playlistSize,
                        playlistCoverUrl = playlistCover.toString()
                    )
                )
            )
            findNavController().navigateUp()
        }
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.tittle.text = "Редактировать"
        binding.buttonCreate.text = "Сохранить"

    }

}