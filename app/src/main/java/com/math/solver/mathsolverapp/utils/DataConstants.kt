package com.math.solver.mathsolverapp.utils

import com.math.solver.mathsolverapp.BuildConfig

object DataConstants {


    val urlHost = if (BuildConfig.DEBUG) {
        "https://app.cscmobicorp.com/aimath/"
    } else {
        "https://app.cscmobicorp.com/aimath/"
    }


}