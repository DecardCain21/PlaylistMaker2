package com.marat.hvatit.playlistmaker2.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val playlistSize: String,
    val playlistCoverUrl: String,
    val playlistDescription: String
)