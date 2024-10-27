package com.marat.hvatit.playlistmaker2.data

import com.marat.hvatit.playlistmaker2.data.dto.TrackSearchRequest
import com.marat.hvatit.playlistmaker2.data.dto.TrackSearchResponse
import com.marat.hvatit.playlistmaker2.domain.api.repository.TrackRepository
import com.marat.hvatit.playlistmaker2.domain.models.Track
import com.marat.hvatit.playlistmaker2.presentation.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTrackCoroutine(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequestCoroutine(TrackSearchRequest(expression))
        when(response.resultCode){
            -1->{
                emit(Resource.Error("-1"))
            }
            200->{
                with(response as TrackSearchResponse){
                    val data = results.mapNotNull {
                        Track(
                            it.trackId ,
                            it.trackName ,
                            it.artistName ,
                            it.trackTimeMillis ,
                            it.artworkUrl100 ,
                            it.country ,
                            it.genre ,
                            it.year ,
                            it.album ,
                            it.previewUrl,
                            "0"
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