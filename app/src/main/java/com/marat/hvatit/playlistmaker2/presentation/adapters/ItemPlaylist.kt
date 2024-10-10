package com.marat.hvatit.playlistmaker2.presentation.adapters

import com.marat.hvatit.playlistmaker2.domain.models.Playlist

data class ItemPlaylist(val data: Playlist, val type: Int) {
    companion object {
        const val TYPE_VERTICAL = 0
        const val TYPE_HORIZONTAL = 1
    }
}
