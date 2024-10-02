package com.marat.hvatit.playlistmaker2.data.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PlaylistWithTrack(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "trackId",
        associateBy = Junction(PlaylistCrossRefEntity::class)
    )
    val tracks: List<PlaylistTrackEntity>

)