package com.math.solver.mathsolverapp.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.math.solver.mathsolverapp.R
import com.math.solver.mathsolverapp.base.viewmodel.BaseViewModel
import com.math.solver.mathsolverapp.databinding.FragmentSettingsBinding
import com.math.solver.mathsolverapp.fragment.BaseFragment
import com.math.solver.mathsolverapp.ui.activity.LanguageActivity
import com.math.solver.mathsolverapp.ui.activity.LoginActivity
import com.math.solver.mathsolverapp.utils.Constants.Companion.FROM_MENU
import com.math.solver.mathsolverapp.utils.clickOnce
import com.math.solver.mathsolverapp.utils.gotoStore
import com.math.solver.mathsolverapp.utils.openActivity
import com.math.solver.mathsolverapp.utils.showDialogSafely
import com.math.solver.mathsolverapp.utils.showToast

class SettingsFragment : BaseFragment<BaseViewModel,FragmentSettingsBinding>(BaseViewModel::class){
    companion object {
        fun newInstance(
        ): SettingsFragment {
            return SettingsFragment()
        }
    }
    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater)
    }

    override fun initView() {
    }


    override fun clickView() {
        super.clickView()
        viewBinding.apply {
            btnLogin.clickOnce {
                requireContext().openActivity(LoginActivity::class.java)
            }
            Btnlanguage.clickOnce {
                requireContext().openActivity(LanguageActivity::class.java){
                    putBoolean(FROM_MENU,true)
                }
            }
            BtnRate.clickOnce {
            }

            BtnVersion.clickOnce {
                clickVersion()

            }

        }
    }
//    private fun clickRating() {
//        val popupRating = RatingDialog().newInstance()
//        popupRating.setCallback(object : RatingDialog.ConfirmCallback {
//            override fun clickNegative() {
//            }
//
//            override fun clickPositive(value: Float) {
//                if (value > 4) {
//                    requireContext().gotoStore(true)
//                }
//            }
//        })
//        showDialogSafely(childFragmentManager, popupRating, "RatingDialog")
//    }
    private fun clickVersion() {
        requireContext().showToast(getString(R.string.latest_version))
    }
}

