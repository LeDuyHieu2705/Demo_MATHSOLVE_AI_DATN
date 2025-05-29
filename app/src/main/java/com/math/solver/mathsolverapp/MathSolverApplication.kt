package com.math.solver.mathsolverapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.math.solver.mathsolverapp.di.fileModulesList
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
            modules( fileModulesList)

        }
    }
}