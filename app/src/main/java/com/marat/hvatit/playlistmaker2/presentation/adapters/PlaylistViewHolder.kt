package com.marat.hvatit.playlistmaker2.presentation.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import com.marat.hvatit.playlistmaker2.presentation.utils.GlideHelper

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
        glide.setImage(
            context = itemView.context,
            file = model.playlistCoverUrl,
            imageView = playlistCover
        )
        //Log.e("bind cover", model.playlistCoverUrl)
    }
}