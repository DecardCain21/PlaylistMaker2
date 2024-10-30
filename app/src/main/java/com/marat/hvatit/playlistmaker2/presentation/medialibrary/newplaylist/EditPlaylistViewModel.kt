package com.marat.hvatit.playlistmaker2.presentation.medialibrary.newplaylist

import androidx.lifecycle.viewModelScope
import com.marat.hvatit.playlistmaker2.domain.api.usecase.playlists.AddPlaylistUseCase
import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import kotlinx.coroutines.launch

class EditPlaylistViewModel(addPlaylistUseCase: AddPlaylistUseCase) :
    NewPlaylistViewModel(addPlaylistUseCase) {
    override fun createPlaylist(
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
                    playlistId = playlistId,
                    playlistName = saveEditTextName,
                    playlistSize = playlistSize,
                    playlistCoverUrl = covername,
                    playlistDescription = saveEditTextDescription
                )
            )
        }
    }


}