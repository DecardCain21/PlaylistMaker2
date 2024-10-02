package com.marat.hvatit.playlistmaker2.data.db.converters

import com.marat.hvatit.playlistmaker2.data.db.entity.TrackEntity
import com.marat.hvatit.playlistmaker2.data.dto.TrackDto
import com.marat.hvatit.playlistmaker2.domain.models.Track

class TrackDbConvertor {

    fun map(track: TrackDto): TrackEntity {
        return TrackEntity(
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

    fun map(track: TrackEntity): Track {
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

    fun convertToEntity(track: Track): TrackEntity {
        return TrackEntity(
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

}