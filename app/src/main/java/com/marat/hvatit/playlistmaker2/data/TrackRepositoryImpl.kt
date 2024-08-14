package com.marat.hvatit.playlistmaker2.data

import com.marat.hvatit.playlistmaker2.data.dto.TrackSearchRequest
import com.marat.hvatit.playlistmaker2.data.dto.TrackSearchResponse
import com.marat.hvatit.playlistmaker2.domain.api.repository.TrackRepository
import com.marat.hvatit.playlistmaker2.domain.models.Track
import com.marat.hvatit.playlistmaker2.presentation.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    /*override fun searchTrack(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("ERROR1111!")
            }

            200 -> {
                Resource.Success((response as TrackSearchResponse).results.map {
                    Track(
                        it.trackId ?: "0",
                        it.trackName ?: "0",
                        it.artistName ?: "0",
                        it.trackTimeMillis ?: "0",
                        it.artworkUrl100 ?: "0",
                        it.country ?: "0",
                        it.genre ?: "0",
                        it.year ?: "0",
                        it.album ?: "0",
                        it.previewUrl ?: "0"
                    )
                })
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }*/

    override fun searchTrackCoroutine(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequestCoroutine(TrackSearchRequest(expression))
        when(response.resultCode){
            -1->{
                emit(Resource.Error("-1"))
            }
            200->{
                with(response as TrackSearchResponse){
                    val data = results.map {
                        Track(
                            it.trackId ?: "0",
                            it.trackName ?: "0",
                            it.artistName ?: "0",
                            it.trackTimeMillis ?: "0",
                            it.artworkUrl100 ?: "0",
                            it.country ?: "0",
                            it.genre ?: "0",
                            it.year ?: "0",
                            it.album ?: "0",
                            it.previewUrl ?: "0"
                        )
                    }
                    emit(Resource.Success(data))
                }
            }
            else->{
                emit(Resource.Error("Server Error"))
            }
        }
    }
}