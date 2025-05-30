package com.hieuld.datn.mathsolved.ui.activity

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import com.hieuld.datn.mathsolved.R
import com.hieuld.datn.mathsolved.base.activity.BaseActivity
import com.hieuld.datn.mathsolved.base.viewmodel.BaseViewModel
import com.hieuld.datn.mathsolved.databinding.ActivitySplashBinding
import com.hieuld.datn.mathsolved.utils.commons.utils.isInternetConnected
import com.hieuld.datn.mathsolved.utils.commons.utils.openActivityWithClearTask
import org.koin.android.BuildConfig

class SplashActivity : BaseActivity<BaseViewModel, ActivitySplashBinding>(BaseViewModel::class) {

    private val maxProgress = 100
    private val progressIncrement = 1
    private val progressInterval = 25L // Milliseconds

    private var isShowingOpenAds = false

    private var countLoad = 0
    private var numberOfLoad = 3

    private var isLoadingApp = false


    private var attemptCount = 0
    private var maxAttempts = 3
    private val intervalLoadingTime = 3000L // Milliseconds
    private var adAttemptRunnable: Runnable = Runnable {}




    override fun inflateViewBinding(inflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(inflater)
    }

    override fun initView() {

        if (!diFileComponent.sharedPreferenceUtils.is_tracking_first_open_slash) {
            diFileComponent.sharedPreferenceUtils.is_tracking_first_open_slash = true
//            adr_first_open_app.postFirebaseEvent()
        }

        diFileComponent.sharedPreferenceUtils.isAppPurchased = true

        viewBinding.textView.text = getString(R.string.app_name)


        handler.postDelayed({
            onNextToHomeScreen()
        }, 3000)

    }


    private fun initializeMobileAdsSdk() {

//        (application as FindPhoneApplication).initAds()

        Handler().postDelayed({
            if (!isShowingOpenAds && !isFinishing) {
                onNextToHomeScreen()
            }
        }, 20000)

        try {

            if (!isLoadingApp) {
                initLoading()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            onNextToHomeScreen()
        }


        if (!isLoadingApp) {

        }


    }

    private fun initLoading() {



        isLoadingApp = true


        handler.postDelayed({
            if (!isShowingOpenAds && !isFinishing) {
                onNextToHomeScreen()
            }
        }, 15000)
        //welcome screen new

        adAttemptRunnable = Runnable {
            attemptCount++
            if (attemptCount < maxAttempts && !diFileComponent.sharedPreferenceUtils.isAppPurchased) {


            } else {
                onNextToHomeScreen()
            }
        }

        handler.postDelayed(adAttemptRunnable, intervalLoadingTime)


        var currentProgress = 0
        handler.post(object : Runnable {
            override fun run() {
                if (currentProgress < maxProgress) {
                    currentProgress += progressIncrement
                    viewBinding.customProgressBar.progress = currentProgress
                    handler.postDelayed(this, progressInterval)
                } else {
                    // Đạt đến giá trị tối đa
                    countLoad++
                    if (countLoad < 3) {
                        currentProgress = 0
                        handler.postDelayed(this, progressInterval)
                    }
                }
            }
        })
    }

    private fun initOpenApp() {

        Handler(Looper.getMainLooper()).postDelayed({

        }, 2500)

        if (BuildConfig.DEBUG) {
//            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false)
        }

        if (isInternetConnected()) {
            onLoginGSM()

        }
    }

    private fun onLoginGSM() {

    }

    private fun initNotPurchase() {
        viewBinding.tvAds.visibility = View.VISIBLE
    }


    private fun onNextToHomeScreen() {
        isShowingOpenAds = false
        if (!isFinishing) {
            if (diFileComponent.sharedPreferenceUtils.isSecondOpenApp) {
                openActivityWithClearTask(LanguageActivity::class.java)
                finish()
            } else {
                openActivityWithClearTask(LanguageActivity::class.java)
                finish()
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        try {
            handler.removeCallbacks(adAttemptRunnable)
            handler.removeCallbacksAndMessages(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}