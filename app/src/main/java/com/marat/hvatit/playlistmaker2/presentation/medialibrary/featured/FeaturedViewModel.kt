package com.marat.hvatit.playlistmaker2.presentation.medialibrary.featured

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marat.hvatit.playlistmaker2.domain.api.usecase.tracks.GetFavoriteTracksUseCase
import com.marat.hvatit.playlistmaker2.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FeaturedViewModel(
    private val getFavoriteTracksUseCase: GetFavoriteTracksUseCase
) : ViewModel() {

    private var featuredState: FeaturedState = FeaturedState.Data(emptyList())
    private var loadingFeaturedData = MutableLiveData(featuredState)

    fun getFeaturedState(): LiveData<FeaturedState> = loadingFeaturedData

    fun getFeaturedTracks() {
        viewModelScope.launch(Dispatchers.IO) {
            getFavoriteTracksUseCase.execute().catch { _ -> }
                .collect { tracks ->
                    setDataState(tracks)
                }
        }
    }

    private fun setDataState(data: List<Track>) {
        if (data.isEmpty()) {
            loadingFeaturedData.postValue(FeaturedState.EmptyState)
        } else {
            loadingFeaturedData.postValue(FeaturedState.Data(data.reversed()))
        }
    }
}