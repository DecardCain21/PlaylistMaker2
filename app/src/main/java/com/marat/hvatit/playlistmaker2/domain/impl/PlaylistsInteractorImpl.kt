package com.marat.hvatit.playlistmaker2.domain.impl

import com.marat.hvatit.playlistmaker2.data.db.entity.PlaylistWithTrack
import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import com.marat.hvatit.playlistmaker2.domain.playlists.PlaylistsInteractor
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl: PlaylistsInteractor {
    override suspend fun savePlaylist(playlist: Playlist) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        TODO("Not yet implemented")
    }

    override fun getPlaylist(): Flow<List<PlaylistWithTrack>> {
        TODO("Not yet implemented")
    }
}