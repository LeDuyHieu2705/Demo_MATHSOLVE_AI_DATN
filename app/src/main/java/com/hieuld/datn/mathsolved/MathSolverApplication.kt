package com.hieuld.datn.mathsolved

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.hieuld.datn.mathsolved.di.fileModulesList
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MathSolverApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        initKoin()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }

    private fun initKoin() {
        startKoin {
            androidContext(this@MathSolverApplication)
            modules(fileModulesList)

        }
    }
}