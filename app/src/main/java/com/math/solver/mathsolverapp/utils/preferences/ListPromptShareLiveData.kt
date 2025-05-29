package com.math.solver.mathsolverapp.utils.preferences

import android.content.SharedPreferences
import androidx.lifecycle.LiveData

class ListPromptShareLiveData(
    private val sharedPreferences: SharedPreferences,
    private val key: String,
    private val defValue: String
): LiveData<String>() {

    private val preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
        if (key == changedKey) {
            value = sharedPreferences.getString(key, defValue)
        }
    }

    override fun onActive() {
        super.onActive()
        value = sharedPreferences.getString(key, defValue)
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onInactive() {
        super.onInactive()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
    }


}