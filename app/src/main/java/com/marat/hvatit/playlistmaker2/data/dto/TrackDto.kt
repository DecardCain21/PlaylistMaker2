package com.marat.hvatit.playlistmaker2.data.dto

import com.google.gson.annotations.SerializedName

class TrackDto(
    @SerializedName("trackId")
    val trackId: String,
    @SerializedName("trackName")
    val trackName: String,
    @SerializedName("artistName")
    val artistName: String,
    @SerializedName("trackTimeMillis")
    val trackTimeMillis: String,
    @SerializedName("artworkUrl100")
    val artworkUrl100: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("primaryGenreName")
    val genre: String,
    @SerializedName("releaseDate")
    val year: String,
    @SerializedName("collectionName")
    val album: String,
    @SerializedName("previewUrl")
    val previewUrl: String
)
