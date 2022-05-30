package com.asl.lamdaclculator.data

import android.content.Context
import android.content.SharedPreferences

class PreferenceRepository(context: Context) {
    private var sharedPref : SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    var theme : Int
    get() = sharedPref.getInt("theme", -1)
    set(value) {
        val editor = sharedPref.edit()
        editor.putInt("theme", value)
        editor.apply()
    }
}