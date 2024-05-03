package com.marat.hvatit.playlistmaker2.domain.models

data class Track(
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