package com.hieuld.datn.mathsolved.ui.activity

import android.view.LayoutInflater
import com.hieuld.datn.mathsolved.base.activity.BaseActivity
import com.hieuld.datn.mathsolved.base.viewmodel.BaseViewModel
import com.hieuld.datn.mathsolved.databinding.FragmentSettingsBinding
import com.hieuld.datn.mathsolved.utils.commons.utils.clickOnce
import com.hieuld.datn.mathsolved.utils.commons.utils.openActivity

class SettingActivity : BaseActivity<BaseViewModel, FragmentSettingsBinding>(BaseViewModel::class) {


    override fun inflateViewBinding(inflater: LayoutInflater): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater)
    }

//    override fun loadAds() {
//        super.loadAds()
//        diFileComponent.frameAds.loadAdsToFrame(
//            typeOfAds = AdTypeConfigEnum.NATIVE.type,
//            activity = this,
//            adsPlaceHolder = viewBinding.layoutAds,
//            bannerType = BannerType.ADAPTIVE_BANNER,
//            nativeType = NativeType.FIX,
//            isBottom = false,
//            bannerCallBack = object : BannerCallBack {
//                override fun onAdFailedToLoad(adError: String) {
//
//                }
//
//                override fun onAdLoaded() {
//                }
//
//                override fun onAdImpression() {
//
//                }
//
//                override fun onPreloaded() {
//
//                }
//
//                override fun onAdClicked() {
//
//                }
//
//                override fun onAdClosed() {
//
//                }
//
//                override fun onAdOpened() {
//
//                }
//
//                override fun onAdSwipeGestureClicked() {
//
//                }
//
//            }
//        )
//    }

    override fun initView() {

        viewBinding.apply {


        }
    }


    override fun clickView() {
        super.clickView()
        viewBinding.apply {
            btnLogin.clickOnce {
                openActivity(LoginActivity::class.java)
            }

        }

//    private fun onSelectLanguage() {
//        openActivity(LanguageActivity::class.java) {
//            putBoolean(Constants.FROM_MENU, true)
//        }
//    }

//    private fun clickRating() {
//        val popupRating = RatingDialog().newInstance()
//        popupRating.setCallback(object : RatingDialog.ConfirmCallback {
//            override fun clickNegative() {
//            }
//
//            override fun clickPositive(value: Float) {
//                if (value > 4) {
//                    gotoStore(true)
//                }
//            }
//        })
//        showDialogSafely(supportFragmentManager, popupRating, "RatingDialog")
//    }

//    private fun clickVersion() {
//        showToast(getString(R.string.latest_version))
//    }

    }
}