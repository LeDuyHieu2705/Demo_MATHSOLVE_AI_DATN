package com.hieuld.datn.mathsolved.utils

import com.hieuld.datn.mathsolved.BuildConfig

object DataConstants {


    val urlHost = if (BuildConfig.DEBUG) {
        "https://app.cscmobicorp.com/aimath/"
    } else {
        "https://app.cscmobicorp.com/aimath/"
    }


}