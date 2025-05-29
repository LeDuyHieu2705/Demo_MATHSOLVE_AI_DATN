package com.math.solver.mathsolverapp.utils.preferences

import android.content.SharedPreferences
import androidx.lifecycle.LiveData

class SharedPreferenceIntLiveData(
    private val sharedPreferences: SharedPreferences,
    private val key: String,
    private val defValue: Int
) : LiveData<Int>() {

    private val preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
        if (key == changedKey) {
            value = sharedPreferences.getInt(key, defValue)
        }
    }

    override fun onActive() {
        super.onActive()
        value = sharedPreferences.getInt(key, defValue)
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onInactive() {
        super.onInactive()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
    }




}