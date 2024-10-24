package com.marat.hvatit.playlistmaker2.data.db.converters

import com.marat.hvatit.playlistmaker2.data.db.entity.PlaylistCrossRefEntity
import com.marat.hvatit.playlistmaker2.data.db.entity.PlaylistEntity
import com.marat.hvatit.playlistmaker2.data.db.entity.PlaylistTrackEntity
import com.marat.hvatit.playlistmaker2.domain.models.Playlist
import com.marat.hvatit.playlistmaker2.domain.models.Track

class PlaylistDbConvertor {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.playlistId.toInt(),
            playlist.playlistName,
            playlist.playlistSize,
            playlist.playlistCoverUrl,
            playlist.playlistDescription
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.id.toString(),
            playlist.name,
            playlist.playlistSize,
            playlist.playlistCoverUrl,
            playlist.playlistDescription
        )
    }

    fun map(track: PlaylistTrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.country,
            track.genre,
            track.year,
            track.album,
            track.previewUrl,
            track.dateAdd
        )
    }

    fun convertToEntity(track: Track): PlaylistTrackEntity {
        return PlaylistTrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.country,
            track.genre,
            track.year,
            track.album,
            track.previewUrl,
            track.dateAdd
        )
    }
    fun convertToCrossRef(playlistId: String, trackId: String): PlaylistCrossRefEntity {
        return PlaylistCrossRefEntity(id = playlistId.toInt(), trackId = trackId)
    }

    fun convertToTrack(track: PlaylistTrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.country,
            track.genre,
            track.year,
            track.album,
            track.previewUrl,
            track.dateAdd
        )
    }

    fun convertToEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.playlistId.toInt(),
            playlist.playlistName,
            playlist.playlistSize,
            playlist.playlistCoverUrl,
            playlist.playlistDescription
        )
    }

}