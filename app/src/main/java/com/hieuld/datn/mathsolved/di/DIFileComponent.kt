package com.hieuld.datn.mathsolved.di

import com.hieuld.datn.mathsolved.utils.commons.managers.InternetManager
import com.hieuld.datn.mathsolved.utils.commons.preferences.SharedPreferenceUtils
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DIFileComponent : KoinComponent {
    // Utils
    val sharedPreferenceUtils by inject<SharedPreferenceUtils>()

    // Managers
    val internetManager by inject<InternetManager>()
}