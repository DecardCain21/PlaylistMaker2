package com.marat.hvatit.playlistmaker2.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.marat.hvatit.playlistmaker2.data.db.entity.PlaylistCrossRefEntity
import com.marat.hvatit.playlistmaker2.data.db.entity.PlaylistEntity
import com.marat.hvatit.playlistmaker2.data.db.entity.PlaylistTrackEntity
import com.marat.hvatit.playlistmaker2.data.db.entity.PlaylistWithTrack
import com.marat.hvatit.playlistmaker2.data.db.entity.TrackEntity

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(trackEntity: TrackEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylistTrack(playlistTrackEntity: PlaylistTrackEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylistCrossRef(playlistCrossRefEntity: PlaylistCrossRefEntity)

    @Delete(entity = TrackEntity::class)
    fun deleteTrack(trackEntity: TrackEntity)

    @Delete(entity = PlaylistEntity::class)
    fun deletePlaylist(playlist: PlaylistEntity)

    @Delete(entity = PlaylistTrackEntity::class)
    fun deletePlaylistTrack(playlistTrackEntity: PlaylistTrackEntity)

    @Query("SELECT * FROM favorite_tracks_table")
    fun getTracks(): List<TrackEntity>

    @Query("SELECT * FROM playlists_table")
    fun getPlaylists(): List<PlaylistEntity>

    @Transaction
    @Query("SELECT * FROM playlists_table WHERE id = :id")
    fun getPlaylistWorker(id: Int): PlaylistWithTrack

    @Query("SELECT id FROM playlists_table")
    fun getPlaylistIds():List<Int>

    @Query("UPDATE playlists_table SET playlistSize = :newSize WHERE id = :playlistId")
    suspend fun updatePlaylistSize(playlistId: Int, newSize: String): Int

    @Query("DELETE FROM playlist_cross_ref_table WHERE id = :playlistId AND trackId = :trackId")
    fun deletePlaylistCrossRef(playlistId: Int, trackId: String): Int

    @Transaction
    @Query("DELETE FROM playlists_track_table WHERE trackId = :playlistTrackId AND NOT EXISTS (SELECT 1 FROM playlist_cross_ref_table WHERE trackId = :playlistTrackId)")
    suspend fun deletePlaylistTrackIfNoReferences(playlistTrackId: Int): Int

}