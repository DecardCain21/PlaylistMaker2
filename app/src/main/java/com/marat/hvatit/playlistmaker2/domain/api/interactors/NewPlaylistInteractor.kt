package com.marat.hvatit.playlistmaker2.domain.api.interactors

import com.marat.hvatit.playlistmaker2.domain.models.Playlist

interface NewPlaylistInteractor {
    suspend fun addPlaylist(playlist: Playlist)

    suspend fun editPlaylist(playlist: Playlist)

    suspend fun getId(): Int
}