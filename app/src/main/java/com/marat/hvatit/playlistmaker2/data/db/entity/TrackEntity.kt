package com.marat.hvatit.playlistmaker2.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tracks_table")
data class TrackEntity(
    @PrimaryKey
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val country: String,
    val genre: String,
    val year: String,
    val album: String,
    val previewUrl: String
)
