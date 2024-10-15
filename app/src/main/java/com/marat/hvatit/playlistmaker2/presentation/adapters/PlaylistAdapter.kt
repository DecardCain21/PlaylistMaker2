package com.marat.hvatit.playlistmaker2.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import com.marat.hvatit.playlistmaker2.presentation.utils.GlideHelper
import org.koin.java.KoinJavaComponent

class PlaylistAdapter : RecyclerView.Adapter<PlaylistViewHolder>() {
    private var playlists: List<Playlist> = emptyList()
    private val glide: GlideHelper by KoinJavaComponent.inject(GlideHelper::class.java)
    var saveTrackToPlaylist: SaveToPlaylistListener? = null

    private var items: List<ItemPlaylist> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return when (viewType) {
            ItemPlaylist.TYPE_VERTICAL -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.playlist_cell, parent, false)
                PlaylistViewHolder(view as ViewGroup, glide)
            }

            ItemPlaylist.TYPE_HORIZONTAL -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.playlist_cell_horizontal, parent, false)
                PlaylistViewHolder(view as ViewGroup, glide)
            }

            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.playlist_cell, parent, false)
                PlaylistViewHolder(view as ViewGroup, glide)
            }
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return items[position].type
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val item: ItemPlaylist = items[position]
        holder.bind(items[position].data)
        if (holder.itemViewType == ItemPlaylist.TYPE_HORIZONTAL) {
            holder.itemView.setOnClickListener {
                saveTrackToPlaylist?.addToPlaylist(item)
            }
        }
        if(holder.itemViewType == ItemPlaylist.TYPE_VERTICAL){
            holder.itemView.setOnClickListener {
                saveTrackToPlaylist?.addToPlaylist(item)
            }
        }
    }

    private fun convertToListItemPlaylist(
        playlists: List<Playlist>,
        type: Int
    ): List<ItemPlaylist> {
        return playlists.map {
            convertToItem(it, type)
        }
    }

    private fun convertToItem(playlists: Playlist, type: Int): ItemPlaylist {
        return ItemPlaylist(data = playlists, type = type)
    }

    fun update(playlists: List<Playlist>, typeItemPlaylist: Int) {
        this.playlists = playlists
        this.items = convertToListItemPlaylist(playlists, typeItemPlaylist)
    }

    fun interface SaveToPlaylistListener {
        fun addToPlaylist(itemPlaylist: ItemPlaylist)
    }

}