package com.marat.hvatit.playlistmaker2.presentation.medialibrary.playlists

import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import com.marat.hvatit.playlistmaker2.presentation.utils.GlideHelper
import java.io.File

class PlaylistViewHolder(itemView: View, private val glide: GlideHelper) :
    RecyclerView.ViewHolder(itemView) {
    private val playlistName: TextView
    private val playlistSize: TextView
    private val playlistCover: ImageView

    init {
        playlistName = itemView.findViewById(R.id.playlist_name)
        playlistSize = itemView.findViewById(R.id.playlist_count)
        playlistCover = itemView.findViewById(R.id.cover)
    }

    fun bind(model: Playlist) {
        playlistName.text = model.playlistName
        playlistSize.text = model.playlistSize
        val filePath = File((Environment.DIRECTORY_PICTURES),"myalbum")
        val file = File(filePath, model.playlistCoverUrl)
        Log.e("bind cover", model.playlistCoverUrl)
        playlistCover.setImageURI(file.toUri())
    }
}