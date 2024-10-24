package com.marat.hvatit.playlistmaker2.domain.impl

import android.util.Log
import com.marat.hvatit.playlistmaker2.domain.api.interactors.NewPlaylistInteractor
import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import com.marat.hvatit.playlistmaker2.domain.playlists.PlaylistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewPlaylistInteractorImpl(private val playlistsRepository: PlaylistsRepository) :
    NewPlaylistInteractor {

    override suspend fun addPlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            playlistsRepository.savePlaylist(playlist)
        }
    }

    override suspend fun editPlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            playlistsRepository.savePlaylist(playlist)
        }
    }

    override suspend fun getId(): Int {
        var listIds = mutableListOf<Int>()
        withContext(Dispatchers.IO) {
            playlistsRepository.getPlaylistsIds().collect { ids -> listIds.addAll(ids) }
            Log.e("listId", "$listIds")
        }
        /*val result = listIds.maxOrNull()
        if (result != null) {
            return generateId(result)
        }*/
        return 0
    }

    private fun generateId(id: Int): Int {
        return id + 1
    }
}