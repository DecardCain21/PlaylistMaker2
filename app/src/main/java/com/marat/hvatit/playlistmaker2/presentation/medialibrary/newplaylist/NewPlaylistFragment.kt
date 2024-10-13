package com.marat.hvatit.playlistmaker2.presentation.medialibrary.newplaylist

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.databinding.FragmentNewplaylistBinding
import com.marat.hvatit.playlistmaker2.presentation.utils.GlideHelper
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class NewPlaylistFragment : Fragment() {

    private var _binding: FragmentNewplaylistBinding? = null
    private val binding
        get() = _binding!!


    private val viewModel: NewPlaylistViewModel by viewModel<NewPlaylistViewModel>()
    private val glide: GlideHelper by inject()


    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    private var saveEditTextName: String? = null
    private var saveEditTextDescription: String? = null
    private var playlistCover: String? = "COVER_IMG_"
    private var coverUri: Uri? = null
    private val callback = object : OnBackPressedCallback(
        getFieldsIsEmpty()
    ) {
        override fun handleOnBackPressed() {
            isEnabled = false
            confirmDialog.show()
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
        binding.etName.addTextChangedListener(textWatcherName())
        binding.etDescription.addTextChangedListener(textWatcherDescription())
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.cover.setImageURI(uri)
                    glide.setImageDb(
                        requireContext(),
                        uri,
                        binding.cover,
                        GlideHelper.VERTICAL_PLAYLIST_CORNER_RADIUS
                    )
                    coverUri = uri
                } else {
                    //error
                }
            }
        binding.cover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        confirmDialog = MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNegativeButton("Отмена") { _, _ ->

            }.setPositiveButton("Завершить") { _, _ ->
                findNavController().navigateUp()
            }
        binding.back.setOnClickListener {
            if (!saveEditTextName.isNullOrEmpty() || !saveEditTextDescription.isNullOrEmpty()) {
                confirmDialog.show()
            } else {
                findNavController().navigateUp()
            }
        }
        binding.buttonCreate.setOnClickListener {
            coverUri?.let { saveImage(it) }
            playlistCover?.let { it1 ->
                viewModel.createPlaylist(
                    it1,
                    saveEditTextName!!,
                    saveEditTextDescription ?: "",
                    onSuccess = {
                        Snackbar.make(
                            view,
                            "Плейлист $saveEditTextName создан",
                            Snackbar.LENGTH_LONG
                        ).show();
                        findNavController().navigateUp()
                    }
                )
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, // LifecycleOwner
            callback
        )
    }

    private fun textWatcherName() = object : TextWatcher {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //empty
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            saveEditTextName = s.toString()
            callback.isEnabled = getFieldsIsEmpty()
            if (s.isNullOrEmpty() || s.isBlank()) {
                binding.buttonCreate.setBackgroundResource(R.drawable.button_create_off)
                binding.buttonCreate.isEnabled = false
                binding.etName.isSelected = false
            } else {
                binding.buttonCreate.setBackgroundResource(R.drawable.button_create_on)
                binding.buttonCreate.isEnabled = true
                binding.etName.isSelected = true
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    binding.etName.setTextCursorDrawable(R.drawable.text_fields_name)
                }*/
            }
        }

    }

    private fun textWatcherDescription() = object : TextWatcher {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //empty
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            saveEditTextDescription = s.toString()
            callback.isEnabled = getFieldsIsEmpty()
            binding.etDescription.isSelected = !s.isNullOrEmpty()
        }

    }

    private fun getFieldsIsEmpty(): Boolean {
        Log.e(
            "getFieldsIsEmpty",
            "${!saveEditTextName.isNullOrEmpty() || !saveEditTextDescription.isNullOrEmpty()}"
        )
        return !saveEditTextName.isNullOrEmpty() || !saveEditTextDescription.isNullOrEmpty()
    }

    private fun saveImage(uri: Uri) {
        val filePath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "Myalbum"
        )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        playlistCover += System.currentTimeMillis()
        val file = File(filePath, "$playlistCover.jpg")
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        viewModel.saveImageToPrivateStorage(inputStream, outputStream)
    }


}