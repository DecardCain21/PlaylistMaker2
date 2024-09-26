package com.marat.hvatit.playlistmaker2.data.db.converters

import com.marat.hvatit.playlistmaker2.data.db.entity.PlaylistCrossRefEntity
import com.marat.hvatit.playlistmaker2.data.db.entity.PlaylistEntity
import com.marat.hvatit.playlistmaker2.data.db.entity.PlaylistWithTrack
import com.marat.hvatit.playlistmaker2.data.db.entity.TrackEntity
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

    fun map(playlistWithTrack: PlaylistWithTrack): List<Track> {
        val result = mutableListOf<Track>()
        for (i in playlistWithTrack.tracks) {
            result.add(convertToTrack(i))
        }
        return result
    }

    fun convertToCrossRef(playlistId: String, trackId: String): PlaylistCrossRefEntity {
        return PlaylistCrossRefEntity(id = playlistId.toInt(), trackId = trackId)
    }

    fun convertToTrack(track: TrackEntity): Track {
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
            track.previewUrl
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