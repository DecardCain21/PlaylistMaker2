package com.marat.hvatit.playlistmaker2.presentation.medialibrary.featured

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marat.hvatit.playlistmaker2.domain.favorites.FavoritesInteractor
import com.marat.hvatit.playlistmaker2.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FeaturedViewModel(private val interactorDb: FavoritesInteractor) : ViewModel() {

    private var featuredState: FeaturedState = FeaturedState.Data(emptyList())
    private var loadingFeaturedData = MutableLiveData(featuredState)

    fun getFeaturedState(): LiveData<FeaturedState> = loadingFeaturedData

    fun getFeaturedTracks() {
        viewModelScope.launch(Dispatchers.IO) {
            interactorDb.addFavorite().catch { exception -> }
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