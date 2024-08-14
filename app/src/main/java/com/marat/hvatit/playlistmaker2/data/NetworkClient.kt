package com.marat.hvatit.playlistmaker2.data

import com.marat.hvatit.playlistmaker2.data.dto.Response

interface NetworkClient {
    suspend fun doRequestCoroutine(dto: Any): Response

}