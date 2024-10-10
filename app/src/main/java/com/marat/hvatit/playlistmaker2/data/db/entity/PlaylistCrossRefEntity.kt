package com.marat.hvatit.playlistmaker2.data.db.entity

import androidx.room.Entity

@Entity(
    tableName = "playlist_cross_ref_table",
    primaryKeys = ["id","trackId"]
)
data class PlaylistCrossRefEntity(
    val id: Int,
    val trackId: String,
)