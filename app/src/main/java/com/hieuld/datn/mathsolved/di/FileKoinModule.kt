package com.hieuld.datn.mathsolved.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.hieuld.datn.mathsolved.R
import com.hieuld.datn.mathsolved.utils.commons.managers.InternetManager
import com.hieuld.datn.mathsolved.utils.commons.preferences.SharedPreferenceUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


private val managerModules = module {
    single { InternetManager(androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager) }
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

private val applicationScope = CoroutineScope(SupervisorJob())

private val dbFileModule = module {


}

val fileModulesList = listOf(dbFileModule, managerModules, utilsModules)