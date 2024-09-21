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
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.databinding.FragmentNewplaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class NewPlaylistFragment : Fragment() {

    private var _binding: FragmentNewplaylistBinding? = null
    private val binding
        get() = _binding!!


    private val viewModel: NewPlaylistViewModel by viewModel<NewPlaylistViewModel>()


    lateinit var confirmDialog: MaterialAlertDialogBuilder

    private var saveEditTextName: String? = null
    private var saveEditTextDescription: String? = null
    private var playlistCover: String? = "COVER_IMG_"
    private var coverUri: Uri? = null


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
                    Log.d("PhotoPicker", "Media selected")
                    binding.cover.setImageURI(uri)
                    coverUri = uri
                    Log.d("PhotoPicker","$coverUri")
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        binding.cover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        confirmDialog = MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNegativeButton("Отмена") { dialog, which ->

            }.setPositiveButton("Завершить") { dialog, which ->
                findNavController().navigateUp()
            }
        binding.back.setOnClickListener {
            confirmDialog.show()
        }
        binding.buttonCreate.setOnClickListener {
            saveImage(coverUri!!)
            playlistCover?.let { it1 ->
                viewModel.createPlaylist(
                    it1,
                    saveEditTextName!!,
                    saveEditTextDescription!!,
                    onSuccess = {
                        //somf
                    }
                )
            }
        }
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
            if (s.isNullOrEmpty()) {
                binding.buttonCreate.setBackgroundResource(R.drawable.button_create_off)
                binding.buttonCreate.isEnabled = false
            } else {
                binding.buttonCreate.setBackgroundResource(R.drawable.button_create_on)
                binding.buttonCreate.isEnabled = true
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
        }

    }

    private fun saveImage(uri: Uri) {
        val filePath = File((Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        /*val currentDate = Date()
        val timeFormat: DateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val timeText = timeFormat.format(currentDate)*/
        playlistCover += System.currentTimeMillis()
        val file = File(filePath, "$playlistCover.jpg")
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        viewModel.saveImageToPrivateStorage(inputStream, outputStream)
        /*BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)*/
    }


}