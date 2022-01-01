package com.renad.sudoku.ui.main_activity.fragments.settings_fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.renad.sudoku.ui.main_activity.MainActivity
import com.renad.sudoku.R
import com.renad.sudoku.databinding.SettingsFragmentBinding
import java.util.*


class SettingsFragment : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme


    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var binding: SettingsFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        val sharedPref = requireActivity().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        binding.colorsModeToggleButton.addOnButtonCheckedListener { ToggleButtonGroup, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.darkBtn -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        editor.putBoolean("DARK_MOOD",true).apply()
                    }
                    R.id.lightBtn -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        editor.putBoolean("DARK_MOOD",false).apply()
                    }
                }
            }
        }



        binding.LanguageToggleButton.addOnButtonCheckedListener { ToggleButtonGroup, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.ArabicButton -> activity?.let {
                        editor.putString("LOCALE", "ar").apply()
                        setLocale(it, "ar")
                    }
                    R.id.EnglishButton -> activity?.let {
                        val editor = sharedPref.edit()
                        editor.putString("LOCALE", "en").apply()
                        setLocale(it, "en")
                    }
                }
            }
        }
    }

    fun setLocale(activity: Activity, lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val resources = activity.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        startActivity(Intent(activity, MainActivity::class.java))
        activity.finish()
    }

}

