package com.hieuld.datn.mathsolved.base.viewmodel

import androidx.lifecycle.ViewModel
import com.hieuld.datn.mathsolved.di.DIFileComponent

abstract class BaseViewModel : ViewModel() {

    protected val diFileComponent by lazy { DIFileComponent() }

}
