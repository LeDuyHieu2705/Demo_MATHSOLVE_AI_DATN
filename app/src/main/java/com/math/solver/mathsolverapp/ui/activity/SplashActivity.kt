package com.math.solver.mathsolverapp.ui.activity

import android.view.LayoutInflater
import android.view.View
import com.math.solver.mathsolverapp.R
import com.math.solver.mathsolverapp.base.activity.BaseActivity
import com.math.solver.mathsolverapp.base.viewmodel.BaseViewModel
import com.math.solver.mathsolverapp.databinding.ActivitySplashBinding
import com.math.solver.mathsolverapp.utils.openActivityWithClearTask

class SplashActivity : BaseActivity<BaseViewModel, ActivitySplashBinding>(BaseViewModel::class) {

    private var isShowingOpenAds = false

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

        initNotPurchase()

        handler.postDelayed({
            onNextToHomeScreen()
        }, 3000)
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