package com.marat.hvatit.playlistmaker2.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.marat.hvatit.playlistmaker2.data.db.dao.TrackDao
import com.marat.hvatit.playlistmaker2.data.db.entity.PlaylistCrossRefEntity
import com.marat.hvatit.playlistmaker2.data.db.entity.PlaylistEntity
import com.marat.hvatit.playlistmaker2.data.db.entity.PlaylistTrackEntity
import com.marat.hvatit.playlistmaker2.data.db.entity.TrackEntity

@Database(
    version = 2,
    entities = [TrackEntity::class, PlaylistEntity::class, PlaylistCrossRefEntity::class,PlaylistTrackEntity::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao

}