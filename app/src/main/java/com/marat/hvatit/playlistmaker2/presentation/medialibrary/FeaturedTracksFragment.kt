package com.marat.hvatit.playlistmaker2.presentation.medialibrary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.marat.hvatit.playlistmaker2.databinding.FeaturedtracksFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FeaturedTracksFragment : Fragment() {
    companion object {
        private const val TESTSTRING = "Android"

        fun newInstance(str: String) = FeaturedTracksFragment().apply {
            arguments = Bundle().apply {
                putString(TESTSTRING, str)
            }
        }
    }

    private val featuredTracksViewModel: FeaturedViewModel by viewModel {
        parametersOf(requireArguments().getString(TESTSTRING))
    }

    private lateinit var binding: FeaturedtracksFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FeaturedtracksFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}