package com.marat.hvatit.playlistmaker2.presentation.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.marat.hvatit.playlistmaker2.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TAG = "SettingsActivity"


class SettingsFragment : Fragment() {
    private val viewModel by viewModel<SettingsViewModel>()
    private var _binding: FragmentSettingsBinding? = null
    private val binding
        get() = _binding!!

    companion object {
        fun getIntent(context: Context, message: String): Intent {
            return Intent(context, SettingsFragment::class.java).apply {
                putExtra(TAG, message)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.llshare.setOnClickListener { viewModel.createIntent(ActionFilter.SHARE) }
        binding.llsupport.setOnClickListener { viewModel.createIntent(ActionFilter.SUPPORT) }
        binding.llagreement.setOnClickListener { viewModel.createIntent(ActionFilter.USERAGREEMENT) }
        //.....................................................

        /*buttonBack.setOnClickListener {
            onBackPressed()
        }*/

        binding.bswitch.setOnCheckedChangeListener { _, isChecked ->
            switchTheme(isChecked)
        }
        viewModel.getLoadingLiveData().observe(viewLifecycleOwner) {
            switchTheme(it)
        }
    }

    private fun switchTheme(isChecked: Boolean) {
        binding.bswitch.isChecked = isChecked
        val mode =
            if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
        viewModel.storeMode(isChecked)
    }

}



