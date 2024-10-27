package com.marat.hvatit.playlistmaker2.presentation.search

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.data.JsonParserImpl
import com.marat.hvatit.playlistmaker2.databinding.FragmentSearchBinding
import com.marat.hvatit.playlistmaker2.presentation.adapters.TrackListAdapter
import com.marat.hvatit.playlistmaker2.presentation.audioplayer.AudioPlayerFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


const val EDITTEXT_TEXT = "EDITTEXT_TEXT"
private const val TAG = "SearchActivity"

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding
        get() = _binding!!


    private var saveEditText: String? = null

    private val gsonParser: JsonParserImpl by inject()

    //JsonParserImpl
    private val viewModel by viewModel<SearchViewModel>()

    private val trackListAdapter = TrackListAdapter()
    private lateinit var layoutManager: LinearLayoutManager

    companion object {

        private const val CLICK_DEBOUNCE_DELAY = 1000L

        fun getIntent(context: Context, message: String): Intent {
            return Intent(context, SearchFragment::class.java).apply {
                putExtra(TAG, message)
            }
        }
    }

    private var searchText: String? = null
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager = LinearLayoutManager(requireContext())
        binding.songlist.layoutManager = layoutManager
        binding.songlist.adapter = trackListAdapter

        viewModel.getLoadingLiveData().observe(viewLifecycleOwner) { searchState ->
            onState(searchState)
        }

        if (savedInstanceState != null) {
            binding.editText.setText(saveEditText)
        }

        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.setSavedTracks()
                trackListAdapter.notifyDataSetChanged()
            } else {
                if(saveEditText.isNullOrEmpty()) {
                    viewModel.changeState(SearchState.ClearState)
                }
            }
        }

        binding.editText.addTextChangedListener(textWatcher(binding.buttonClear))

        binding.buttonClear.setOnClickListener {
            binding.editText.requestFocus()
            binding.editText.setText("")
            (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                binding.editText.windowToken, 0
            )
            viewModel.setSavedTracks()
        }

        binding.editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchCoroutine(binding.editText.text.toString())
            }
            false
        }

        binding.clearhistory.setOnClickListener {
            binding.clearhistory.isGone = true
            binding.headerhistory.isGone = true

            viewModel.clearSaveStack()
            trackListAdapter.update(emptyList())
            trackListAdapter.notifyDataSetChanged()
        }

        trackListAdapter.clickTrackListener = TrackListAdapter.ClickTrackListener {
            if (clickDebounce()) {
                viewModel.addSaveSongs(it)
                findNavController().navigate(
                    R.id.action_searchFragment_to_audioPlayerFragment,
                    AudioPlayerFragment.createArgs(gsonParser.objectToJson(it))
                )
            }
        }

        binding.buttonUpdate.setOnClickListener {
            viewModel.searchCoroutine(binding.editText.text.toString())
        }
    }

    private fun textWatcher(buttonClear: ImageButton) = object : TextWatcher {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //empty
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            clearButtonVisibility(s).also { buttonClear.visibility = it }
        }

        override fun afterTextChanged(s: Editable?) {
            saveEditText = s.toString()
            if (s.isNullOrEmpty()) {
                viewModel.setSavedTracks()
                trackListAdapter.notifyDataSetChanged()
            } else {
                searchText = s.toString()
                viewModel.searchDebounce(searchText!!)
            }
        }

    }

    private fun onState(searchState: SearchState) {
        when (searchState) {
            is SearchState.AllFine -> allFineState()
            is SearchState.ClearState -> clearState()
            is SearchState.Data -> dataState(searchState)
            is SearchState.Disconnected -> disconnectedState(searchState)
            is SearchState.Download -> downloadState()
            is SearchState.NothingToShow -> nothingToShowState(searchState)
            is SearchState.StartState -> startState(searchState)
            is SearchState.RestoreDataState -> restoreDataState(searchState)
            else -> {}
        }
        trackListAdapter.notifyDataSetChanged()
    }

    private fun startState(searchState: SearchState.StartState) {
        trackListAdapter.update(searchState.cacheTracks)
        binding.placeholder.isVisible = false
        binding.buttonUpdate.isVisible = false
        binding.texterror.isVisible = false
        binding.progressBar.isVisible = false

        binding.clearhistory.isVisible = true
        binding.headerhistory.isVisible = true
        trackListAdapter.notifyDataSetChanged()
    }

    private fun nothingToShowState(searchState: SearchState.NothingToShow) {
        trackListAdapter.update(emptyList())
        binding.placeholder.setImageResource(R.drawable.nothing_problem)
        binding.placeholder.isVisible = true
        binding.texterror.text = requireContext().getString(searchState.message)
        binding.texterror.isVisible = true

        binding.clearhistory.isVisible = false
        binding.headerhistory.isVisible = false
        binding.progressBar.isVisible = false
    }

    private fun downloadState() {
        trackListAdapter.update(emptyList())
        binding.buttonUpdate.isVisible = false
        binding.placeholder.isVisible = false
        binding.texterror.isVisible = false

        binding.clearhistory.isVisible = false
        binding.headerhistory.isVisible = false
        binding.progressBar.isVisible = true
    }

    private fun disconnectedState(searchState: SearchState.Disconnected) {
        trackListAdapter.update(emptyList())
        binding.placeholder.setImageResource(R.drawable.disconnect_problem)
        binding.placeholder.isVisible = true
        binding.buttonUpdate.isVisible = true
        binding.texterror.text = requireContext().getString(searchState.message)
        binding.texterror.isVisible = true


        binding.clearhistory.isVisible = false
        binding.headerhistory.isVisible = false
        binding.progressBar.isVisible = false
    }

    private fun dataState(searchState: SearchState.Data) {
        trackListAdapter.update(searchState.foundTrack)

        binding.placeholder.isVisible = false
        binding.buttonUpdate.isVisible = false
        binding.texterror.isVisible = false
        binding.progressBar.isVisible = false

        binding.clearhistory.isVisible = false
        binding.headerhistory.isVisible = false
    }

    private fun restoreDataState(searchState: SearchState.RestoreDataState) {
        trackListAdapter.update(searchState.tracks)
        binding.songlist.scrollToPosition(searchState.scrollPosition)


        binding.placeholder.isVisible = false
        binding.buttonUpdate.isVisible = false
        binding.texterror.isVisible = false
        binding.progressBar.isVisible = false

        binding.clearhistory.isVisible = false
        binding.headerhistory.isVisible = false

    }

    private fun clearState() {
        trackListAdapter.update(emptyList())
        binding.buttonUpdate.isVisible = false
        binding.placeholder.isVisible = false
        binding.texterror.isVisible = false

        binding.clearhistory.isVisible = false
        binding.headerhistory.isVisible = false
        binding.progressBar.isVisible = false
    }

    private fun allFineState() {
        binding.buttonUpdate.isVisible = false
        binding.placeholder.isVisible = false
        binding.texterror.isVisible = false
        binding.progressBar.isVisible = false
    }

    override fun onResume() {
        super.onResume()
        isClickAllowed = true
    }

    override fun onStop() {
        super.onStop()
        viewModel.setSaveFragmentState(layoutManager.findFirstCompletelyVisibleItemPosition())
    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.saveTracksToCache()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EDITTEXT_TEXT, saveEditText)
        super.onSaveInstanceState(outState)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            ImageButton.GONE
        } else {
            ImageButton.VISIBLE
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }
}