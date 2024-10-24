package com.marat.hvatit.playlistmaker2.presentation.adapters

import android.util.Log
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
        playlistSize.text =
            "${model.playlistSize} ${itemView.context.getString(if (model.playlistSize.toInt() == 1) R.string.playlistviewholder_track else R.string.playlistviewholder_tracks)}"
        val cornerRadius = when (itemViewType) {
            ItemPlaylist.TYPE_VERTICAL -> GlideHelper.VERTICAL_PLAYLIST_CORNER_RADIUS
            ItemPlaylist.TYPE_HORIZONTAL -> GlideHelper.HORIZONTAL_PLAYLIST_CORNER_RADIUS
            else -> GlideHelper.DEFAULT_CORNER_RADIUS
        }
        Log.e("bindModel","cornerRadius:$cornerRadius")
        glide.setImageDb(
            context = itemView.context,
            covername = model.playlistCoverUrl,
            imageView = playlistCover,
            roundedCornersImage = cornerRadius
        )
    }
}