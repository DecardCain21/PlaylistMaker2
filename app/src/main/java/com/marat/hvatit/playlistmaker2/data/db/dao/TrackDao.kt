package com.marat.hvatit.playlistmaker2.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.marat.hvatit.playlistmaker2.data.db.entity.TrackEntity

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(trackEntity: TrackEntity)

    @Query("SELECT * FROM favorite_tracks_table")
    suspend fun getTracks(): List<TrackEntity>
}