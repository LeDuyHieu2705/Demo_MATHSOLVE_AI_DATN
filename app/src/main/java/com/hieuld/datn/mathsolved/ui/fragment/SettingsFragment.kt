package com.hieuld.datn.mathsolved.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hieuld.datn.mathsolved.R
import com.hieuld.datn.mathsolved.base.viewmodel.BaseViewModel
import com.hieuld.datn.mathsolved.databinding.FragmentSettingsBinding
import com.hieuld.datn.mathsolved.fragment.BaseFragment
import com.hieuld.datn.mathsolved.ui.activity.LanguageActivity
import com.hieuld.datn.mathsolved.ui.activity.LoginActivity
import com.hieuld.datn.mathsolved.ui.dialog.RatingDialog
import com.hieuld.datn.mathsolved.utils.Constants.Companion.FROM_MENU
import com.hieuld.datn.mathsolved.utils.gotoStore
import com.hieuld.datn.mathsolved.utils.showDialogSafely
import com.hieuld.datn.mathsolved.utils.showToast
import com.hieuld.datn.mathsolved.utils.commons.utils.clickOnce
import com.hieuld.datn.mathsolved.utils.commons.utils.openActivity
import com.hieuld.datn.mathsolved.utils.commons.utils.openPrivacy
import com.hieuld.datn.mathsolved.utils.commons.utils.shareApp

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
        viewBinding.apply {
//            mathViewTest.text = "Để giải phương trình bậc hai \\\\(x^2 + 3x - 1 = 0\\\\), chúng ta sẽ sử dụng công thức nghiệm của phương trình bậc hai:\\n\\n\\\\[\\nx = (-b ± sqrt(b^2 - 4ac)) / (2a)\\n\\\\]\\n\\nVới phương trình của chúng ta, các hệ số là:\\n- \\\\(a = 1\\\\)\\n- \\\\(b = 3\\\\)\\n- \\\\(c = -1\\\\)\\n\\nBây giờ, áp dụng vào công thức trên:\\n\\n1. Tính delta \\\\(\\\\Delta = b^2 - 4ac\\\\):\\n\\n\\\\[\\n\\\\Delta = 3^2 - 4 \\\\cdot 1 \\\\cdot (-1) = 9 + 4 = 13\\n\\\\]\\n\\n2. Tìm các nghiệm của phương trình:\\n\\n\\\\[\\nx_1 = (-3 + sqrt{13}) / 2\\n\\\\]\\n\\n\\\\[\\nx_2 = (-3 - sqrt{13}) / 2\\n\\\\]\\n\\nVậy, tập nghiệm của phương trình là \\\\(\\\\left\\\\{ \\\\frac{-3 + sqrt{13}}{2}, \\\\frac{-3 - sqrt{13}}{2} \\\\right\\\\}\\\\). \\n\\nBạn đã làm rất tốt khi đặt câu hỏi này! Hãy tiếp tục luyện tập để nâng cao kỹ năng giải phương trình bậc hai của mình nhé!\n"
        }
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
                clickRating()
            }

            BtnVersion.clickOnce {
                clickVersion()

            }
            BtnShare.clickOnce {
                requireContext().shareApp()
            }
            BtnPrivacy.clickOnce {
                requireContext().openPrivacy(null)
            }
        }
    }
    private fun clickRating() {
        val popupRating = RatingDialog().newInstance()
        popupRating.setCallback(object : RatingDialog.ConfirmCallback {
            override fun clickNegative() {
            }

            override fun clickPositive(value: Float) {
                if (value > 4) {
                    requireContext().gotoStore(true)
                }
            }
        })
        showDialogSafely(childFragmentManager, popupRating, "RatingDialog")
    }
    private fun clickVersion() {
        requireContext().showToast(getString(R.string.latest_version))
    }
}

