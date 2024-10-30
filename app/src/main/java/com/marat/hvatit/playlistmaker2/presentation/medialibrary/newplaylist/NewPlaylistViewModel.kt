package com.marat.hvatit.playlistmaker2.presentation.medialibrary.newplaylist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marat.hvatit.playlistmaker2.domain.api.usecase.playlists.AddPlaylistUseCase
import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.io.InputStream

open class NewPlaylistViewModel(val addPlaylistUseCase: AddPlaylistUseCase) : ViewModel() {

    open fun createPlaylist(
        playlistId: String,
        saveEditTextName: String,
        saveEditTextDescription: String,
        playlistSize: String,
        covername: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            addPlaylistUseCase.execute(
                Playlist(
                    playlistId = addPlaylistUseCase.getId().toString(),
                    playlistName = saveEditTextName,
                    playlistDescription = saveEditTextDescription,
                    playlistSize = playlistSize,
                    playlistCoverUrl = covername
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