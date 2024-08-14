package com.marat.hvatit.playlistmaker2.data.network

import com.marat.hvatit.playlistmaker2.data.dto.TrackSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AppleMusicApiService {
    @GET("/search?entity=song")
    suspend fun searchCoroutine(@Query("term") text: String): TrackSearchResponse
}