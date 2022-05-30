package com.asl.lamdaclculator.ui.fragments.dialogs

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.asl.lamdaclculator.R
import com.asl.lamdaclculator.data.PreferenceRepository

class ThemeDialog : PopDialog() {

    override var config: Int = AppCompatDelegate.getDefaultNightMode()
    override var stateArray: Array<Int> = arrayOf(1, 2, -1)
    override var checkedItem: Int = stateArray.indexOf(config)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        title = requireContext().resources.getString(R.string.select_theme)
        choiceArray = requireContext().resources.getStringArray(R.array.theme_choice)
    }

    override fun positiveListener(checkedItem: Int) {
        if (checkedItem != stateArray.indexOf(config)){
            PreferenceRepository(requireContext()).theme = stateArray[checkedItem]
            AppCompatDelegate.setDefaultNightMode(PreferenceRepository(requireContext()).theme)
            requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }
}