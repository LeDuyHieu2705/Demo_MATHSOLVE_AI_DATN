package com.hieuld.datn.mathsolved.ui.activity

import android.view.LayoutInflater
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.hieuld.datn.mathsolved.base.activity.BaseActivity
import com.hieuld.datn.mathsolved.base.viewmodel.BaseViewModel
import com.hieuld.datn.mathsolved.databinding.ActivityOnboardingBinding
import com.hieuld.datn.mathsolved.ui.adapter.OnBoardAdapter
import com.hieuld.datn.mathsolved.utils.commons.utils.clickOnce
import com.hieuld.datn.mathsolved.utils.commons.utils.hide
import com.hieuld.datn.mathsolved.utils.commons.utils.openActivityWithClearTask
import com.hieuld.datn.mathsolved.utils.commons.utils.show

class OnboardingActivity :
    BaseActivity<BaseViewModel, ActivityOnboardingBinding>(BaseViewModel::class) {

    private var isFirstOpenLoadAds = true


    override fun inflateViewBinding(inflater: LayoutInflater): ActivityOnboardingBinding {
        return ActivityOnboardingBinding.inflate(inflater)
    }

    override fun loadAds() {
        super.loadAds()
    }

    override fun initView() {
        viewBinding.apply {

            val myadapter = OnBoardAdapter(this@OnboardingActivity)

            viewPager.adapter = myadapter

            TabLayoutMediator(indicator, viewPager) { tab, position -> }.attach()

            btnNext.clickOnce {
                if (viewPager.currentItem < 2) {
                    viewPager.currentItem += 1
                } else {
                    openActivityWithClearTask(MainActivity::class.java)
                }
            }


            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when (position) {
                        0 -> {


                            layoutAds.show()
                        }

                        1 -> {
                            layoutAds.hide()
                        }

                        2 -> {
                            layoutAds.show()
                        }

                        else -> {
                            //disable nut bat dau
                            layoutAds.show()
                        }
                    }
                }
            })
        }

    }

}
