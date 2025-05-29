package com.math.solver.mathsolverapp.di

import android.app.Application
import com.math.solver.mathsolverapp.R
import com.math.solver.mathsolverapp.utils.preferences.SharedPreferenceUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


private val applicationScope = CoroutineScope(SupervisorJob())

private val dbFileModule = module {


}
private val utilsModules = module {
    single {
        SharedPreferenceUtils(
            androidContext().getSharedPreferences(
                androidContext().getString(R.string.app_name),
                Application.MODE_PRIVATE
            )
        )
    }
}

val fileModulesList = listOf(dbFileModule, utilsModules)