package com.math.solver.mathsolverapp.ui.activity

import android.view.LayoutInflater
import com.google.android.material.tabs.TabLayoutMediator
import com.math.solver.mathsolverapp.base.activity.BaseActivity
import com.math.solver.mathsolverapp.base.viewmodel.BaseViewModel
import com.math.solver.mathsolverapp.databinding.ActivityOnboardingBinding
import com.math.solver.mathsolverapp.ui.adapter.OnBoardAdapter
import com.math.solver.mathsolverapp.utils.clickOnce
import com.math.solver.mathsolverapp.utils.openActivityWithClearTask

class OnboardingActivity :
    BaseActivity<BaseViewModel, ActivityOnboardingBinding>(BaseViewModel::class) {

    private var isFirstOpenLoadAds = true


    override fun inflateViewBinding(inflater: LayoutInflater): ActivityOnboardingBinding {
        return ActivityOnboardingBinding.inflate(inflater)
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
        }

    }


}
