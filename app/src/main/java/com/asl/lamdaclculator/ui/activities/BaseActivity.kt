package com.asl.lamdaclculator.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.asl.lamdaclculator.data.PreferenceRepository

open class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        AppCompatDelegate.setDefaultNightMode(PreferenceRepository(newBase).theme)
        super.attachBaseContext(newBase)
    }
}