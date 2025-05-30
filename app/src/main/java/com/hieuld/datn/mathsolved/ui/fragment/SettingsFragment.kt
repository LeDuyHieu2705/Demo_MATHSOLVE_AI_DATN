package com.hieuld.datn.mathsolved.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hieuld.datn.mathsolved.R
import com.hieuld.datn.mathsolved.base.viewmodel.BaseViewModel
import com.hieuld.datn.mathsolved.databinding.FragmentSettingsBinding
import com.hieuld.datn.mathsolved.fragment.BaseFragment
import com.hieuld.datn.mathsolved.ui.activity.LanguageActivity
import com.hieuld.datn.mathsolved.ui.dialog.RatingDialog
import com.hieuld.datn.mathsolved.utils.Constants.Companion.FROM_MENU
import com.hieuld.datn.mathsolved.utils.commons.utils.clickOnce
import com.hieuld.datn.mathsolved.utils.commons.utils.openActivity
import com.hieuld.datn.mathsolved.utils.commons.utils.openPrivacy
import com.hieuld.datn.mathsolved.utils.commons.utils.shareApp
import com.hieuld.datn.mathsolved.utils.gotoStore
import com.hieuld.datn.mathsolved.utils.showDialogSafely
import com.hieuld.datn.mathsolved.utils.showToast

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
            mathViewTest.text = "Để tìm tập nghiệm của phương trình bậc hai {x^2 + 3x - 1 = 0}, chúng ta có thể sử dụng công thức nghiệm của phương trình bậc hai: \\n\\n{x = (-b ± √(b² - 4ac)) / 2a}.\\n\\nTrong phương trình này, {a = 1}, {b = 3}, và {c = -1}. Chúng ta sẽ tính toán từng bước:\\n\\n1. Tính {b² - 4ac}:\\n{b² - 4ac = 3² - 4×1×(-1) = 9 + 4 = 13}.\\n\\n2. Áp dụng giá trị vào công thức:\\n{x = (-3 ± √(13)) / 2}.\\n\\nVậy, tập nghiệm của phương trình là:\\n{x_1 = (-3 + √(13)) / 2} và {x_2 = (-3 - √(13)) / 2}.\\n\\nHy vọng bạn thấy lời giải này hữu ích! Nếu có bất kỳ câu hỏi nào khác, đừng ngần ngại hỏi nhé!\n"
        }
    }


    override fun clickView() {
        super.clickView()
        viewBinding.apply {
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

