package com.hieuld.datn.mathsolved.utils.commons.preferences

import android.content.SharedPreferences
import androidx.lifecycle.LiveData

class FloatLiveData(
    private val sharedPreferences: SharedPreferences,
    private val key: String,
    private val defValue: Float
) : LiveData<Float>() {

    private val preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
        if (key == changedKey) {
            value = sharedPreferences.getFloat(key, defValue)
        }
    }

    override fun onActive() {
        super.onActive()
        value = sharedPreferences.getFloat(key, defValue)
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onInactive() {
        super.onInactive()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
    }
}