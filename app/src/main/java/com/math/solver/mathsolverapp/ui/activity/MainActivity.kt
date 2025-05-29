package com.math.solver.mathsolverapp.ui.activity

import android.graphics.Color
import android.view.LayoutInflater
import androidx.viewpager.widget.ViewPager
import com.math.solver.mathsolverapp.base.activity.BaseActivity
import com.math.solver.mathsolverapp.base.viewmodel.BaseViewModel
import com.math.solver.mathsolverapp.databinding.ActivityMainBinding
import com.math.solver.mathsolverapp.ui.adapter.ViewPagerAdapter2
import com.math.solver.mathsolverapp.ui.fragment.CameraFragment
import com.math.solver.mathsolverapp.ui.fragment.SettingsFragment
import com.math.solver.mathsolverapp.utils.setSafeOnClickListener

class MainActivity : BaseActivity<BaseViewModel,ActivityMainBinding>(BaseViewModel::class) {

    val adapterMain = ViewPagerAdapter2(supportFragmentManager)

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }


    private fun setupViewPager(viewPager: ViewPager) {

        adapterMain.addFragment(CameraFragment.newInstance())
//        adapterMain.addFragment(TutorExpertFragment.newInstance())
//        adapterMain.addFragment(AdvisorFragment.newInstance(isFromMain = true))
//        adapterMain.addFragment(ToolsFragment.newInstance())
        adapterMain.addFragment(SettingsFragment.newInstance())
        viewPager.adapter = adapterMain
    }

    override fun initView() {

        viewBinding.apply {
            setupViewPager(viewPager2)

            viewPager2.offscreenPageLimit = 4
            updateNavigationUI(0)


            viewPager2.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    updateNavigationUI(position)

                    when (position) {
                        0 -> {
                            layoutNavigationMain.apply {
                                //show chat fragment


                            }
                        }

                        1 -> {
                            //show prompt
                            layoutNavigationMain.apply {


                            }

                        }

                        2 -> {


                        }

                        3 -> {


                        }
                    }


                }

                override fun onPageScrollStateChanged(state: Int) {
                }

            })
            layoutNavigationMain.apply {
                btnScan.setSafeOnClickListener(500) {
                    val currentTab = viewPager2.currentItem
                    if (currentTab != 0) {
                        viewPager2.currentItem = 0
                    }
                    updateNavigationUI(0)
                }

                btnAI.setSafeOnClickListener(500) {
                    val currentTab = viewPager2.currentItem
                    if (currentTab != 1) {
                        viewPager2.currentItem = 1
                    }
                    updateNavigationUI(1)
                }

                btnTools.setSafeOnClickListener(500) {
                    val currentTab = viewPager2.currentItem
                    if (currentTab != 2) {
                        viewPager2.currentItem = 2
                    }
                    updateNavigationUI(2)
                }

                btnSettings.setSafeOnClickListener(500) {
                    val currentTab = viewPager2.currentItem
                    if (currentTab != 3) {
                        viewPager2.currentItem = 3
                    }
                    updateNavigationUI(3)
                }
            }

        }
    }
    private fun updateNavigationUI(selectedIndex: Int) {
        val activeColor = Color.parseColor("#15B28B")
        val inactiveColor = Color.parseColor("#939191")

        with(viewBinding.layoutNavigationMain) {
            val imageViews = listOf(imvScan, imvAI, imvTools, imvSettings)
            val textViews = listOf(txtScan, txtAI, txtTools, txtSettings)

            for (i in imageViews.indices) {
                imageViews[i].setColorFilter(if (i == selectedIndex) activeColor else inactiveColor)
                textViews[i].setTextColor(if (i == selectedIndex) activeColor else inactiveColor)
            }
        }
    }

}





