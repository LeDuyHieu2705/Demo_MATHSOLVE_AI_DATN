package com.math.solver.mathsolverapp.di

import com.math.solver.mathsolverapp.utils.preferences.SharedPreferenceUtils
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DIFileComponent : KoinComponent {
    val sharedPreferenceUtils by inject<SharedPreferenceUtils>()

}