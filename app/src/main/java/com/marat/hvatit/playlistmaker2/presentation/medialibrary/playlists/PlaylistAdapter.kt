package com.marat.hvatit.playlistmaker2.presentation.medialibrary.playlists

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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_cell, parent, false)
        return PlaylistViewHolder(view as ViewGroup, glide)
    }

    override fun getItemCount() = playlists.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        //val item: Playlist = playlists[position]
        holder.bind(playlists[position])
    }

    fun update(playlists: List<Playlist>){
        this.playlists = playlists
    }

}