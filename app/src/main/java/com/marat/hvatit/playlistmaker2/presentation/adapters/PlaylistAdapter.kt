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

    private var items: List<ItemPlaylist> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        /*val view =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_cell, parent, false)
        return PlaylistViewHolder(view as ViewGroup, glide)*/
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

    override fun getItemCount() = items.size/*playlists.size*/

    override fun getItemViewType(position: Int): Int {
        return items[position].type
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        //val item: Playlist = playlists[position]
        //holder.bind(playlists[position])
        holder.bind(items[position].data)
    }

    private fun convertToListItemPlaylist(playlists: List<Playlist>, type:Int): List<ItemPlaylist> {
        return playlists.map {
            convertToItem(it, type)
        }
    }

    private fun convertToItem(playlists: Playlist, type: Int): ItemPlaylist {
        return ItemPlaylist(data = playlists, type = type)
    }

    fun update(playlists: List<Playlist>,typeItemPlaylist:Int) {
        this.playlists = playlists
        this.items = convertToListItemPlaylist(playlists,typeItemPlaylist)
    }

}