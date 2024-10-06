package com.marat.hvatit.playlistmaker2.presentation.medialibrary.newplaylist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marat.hvatit.playlistmaker2.domain.api.interactors.NewPlaylistInteractor
import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.io.InputStream

class NewPlaylistViewModel(private val newPlaylistInteractor: NewPlaylistInteractor) : ViewModel() {

    fun createPlaylist(
        covername: String,
        saveEditTextName: String,
        saveEditTextDescription: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            newPlaylistInteractor.addPlaylist(
                Playlist(
                    newPlaylistInteractor.getId().toString(),
                    playlistName = saveEditTextName,
                    playlistSize = "0",
                    playlistCoverUrl = covername,
                    playlistDescription = saveEditTextDescription
                )
            )
            onSuccess()
        }
    }

    fun saveImageToPrivateStorage(
        inputStream: InputStream?,
        outputStream: FileOutputStream
    ) {
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }
}