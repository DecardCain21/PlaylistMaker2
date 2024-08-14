package com.marat.hvatit.playlistmaker2.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.domain.api.interactors.SaveTrackInteractor
import com.marat.hvatit.playlistmaker2.domain.api.interactors.TrackInteractor
import com.marat.hvatit.playlistmaker2.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val interactor: TrackInteractor, private var trackRepository: SaveTrackInteractor
) : ViewModel() {

    private var latestSearchText: String? = null

    private var searchJob: Job? = null

    private var searchState: SearchState = if (trackRepository.tracks.isEmpty()) {
        SearchState.ClearState
    } else {
        SearchState.StartState(trackRepository.tracks)
    }


    private var loadingLiveData = MutableLiveData(searchState)


    fun getLoadingLiveData(): LiveData<SearchState> = loadingLiveData

    fun changeState(newState: SearchState) {
        loadingLiveData.postValue(newState)
    }

    fun searchCoroutine(query: String) {
        loadingLiveData.postValue(SearchState.Download)
        viewModelScope.launch {
            interactor
                .searchTrackCoroutine(query)
                .collect { pair ->
                    processingResponse(pair.first, pair.second)
                }
        }
    }

    private fun processingResponse(foundTracks: List<Track>?, errorMessage: String?) {
        if (foundTracks != null ) {
            if (foundTracks.isEmpty()) {
                loadingLiveData.postValue(SearchState.NothingToShow(R.string.act_search_nothing))
            } else {
                loadingLiveData.postValue(SearchState.Data(foundTracks))
            }
        } else {
            loadingLiveData.postValue(SearchState.Disconnected(R.string.act_search_disconnect))
        }
    }

    fun addSaveSongs(item: Track) {
        trackRepository.addSaveSongs(item)
        if (loadingLiveData.value is SearchState.StartState) {
            setSavedTracks()
        }
    }

    fun saveTracksToCache() {
        trackRepository.saveTracksToCache()
    }

    fun clearSaveStack() {
        trackRepository.clearSaveStack()
        saveTracksToCache()
    }

    fun setSavedTracks() {
        if (trackRepository.tracks.isEmpty()) {
            loadingLiveData.postValue(SearchState.ClearState)
        } else {
            loadingLiveData.postValue(
                SearchState.StartState(
                    trackRepository.tracks
                )
            )
        }
    }

    fun searchDebounce(newText: String) {
        if (latestSearchText == newText) {
            return
        }
        latestSearchText = newText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchCoroutine(newText)
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
