package com.marat.hvatit.playlistmaker2.presentation.medialibrary.newplaylist

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import com.marat.hvatit.playlistmaker2.domain.api.interactors.NewPlaylistInteractor
import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import kotlinx.coroutines.launch

class EditPlaylistViewModel(newPlaylistInteractor: NewPlaylistInteractor) :
    NewPlaylistViewModel(newPlaylistInteractor) {
    override fun createPlaylist(
        playlistId: String,
        saveEditTextName: String,
        saveEditTextDescription: String,
        playlistSize: String,
        covername: String,
        onSuccess: () -> Unit
    ) {
        /*super.createPlaylist(
            playlistId = playlistId,
            saveEditTextName = saveEditTextName,
            saveEditTextDescription = saveEditTextDescription,
            playlistSize = playlistSize,
            covername = covername,
            onSuccess = onSuccess
        )*/
        viewModelScope.launch {
            newPlaylistInteractor.addPlaylist(
                Playlist(
                    playlistId = playlistId,
                    playlistName = saveEditTextName,
                    playlistSize = playlistSize,
                    playlistCoverUrl = covername,
                    playlistDescription = saveEditTextDescription
                )
            )
            //onSuccess()
        }
    }


}