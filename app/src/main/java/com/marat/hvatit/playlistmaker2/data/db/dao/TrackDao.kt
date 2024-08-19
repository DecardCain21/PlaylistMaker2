package com.marat.hvatit.playlistmaker2.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.marat.hvatit.playlistmaker2.data.db.entity.TrackEntity

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertTrack(trackEntity: TrackEntity)

    @Query("SELECT * FROM favorite_tracks_table")
     fun getTracks(): List<TrackEntity>

     @Delete(entity = TrackEntity::class)
     fun deleteTrack(trackEntity: TrackEntity)
}