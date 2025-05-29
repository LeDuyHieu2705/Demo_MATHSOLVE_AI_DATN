package com.hieuld.datn.mathsolved.ui.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hieuld.datn.mathsolved.R
import com.hieuld.datn.mathsolved.base.viewmodel.BaseViewModel
import com.hieuld.datn.mathsolved.databinding.DialogRatingBinding
import com.hieuld.datn.mathsolved.fragment.BaseDialogFragment
import com.hieuld.datn.mathsolved.utils.dismissDialogSafely
import com.hieuld.datn.mathsolved.utils.goneView
import com.hieuld.datn.mathsolved.utils.visibleView

class RatingDialog : BaseDialogFragment<BaseViewModel, DialogRatingBinding>(BaseViewModel::class) {



    private var star = 5f
    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogRatingBinding {
        return DialogRatingBinding.inflate(inflater)
    }

    override fun initView() {
        viewBinding.apply {
            starRating.setOnRatingBarChangeListener { _, value, _ ->
                star = value
                when (value) {
                    5f -> {
                        imgTop.setImageResource(R.drawable.rating_img_happy)
                    }

                    4f -> {
                        imgTop.setImageResource(R.drawable.rating_img_happy_2
                        )
                    }
                    3f ->{
                        imgTop.setImageResource(R.drawable.rating_img_sad_3
                        )
                    }
                    2f ->{
                        imgTop.setImageResource(R.drawable.rating_img_sad_4
                        )
                    }

                    else -> {
                        imgTop.setImageResource(R.drawable.rating_img_sad)
                    }
                }
                if (value <= 4) {
                    edtFeedback.visibleView()
                } else {
                    edtFeedback.goneView()
                }
            }

            btnNegative.setOnClickListener {
                mCallback?.clickNegative()
                dismissDialogSafely(requireActivity().supportFragmentManager, "RatingDialog")
            }

//            btnPositive.setOnClickListener {
//                ratingView.goneView()
//                showThanks.visibleView()
//                if (star > 4) {
//                    tvPleaseGoStore.visibleView()
//                }
//                Handler(Looper.getMainLooper()).postDelayed({
//                    mCallback?.clickPositive(star)
//                    dismissDialogSafely(requireActivity().supportFragmentManager, "RatingDialog")
//                }, 1000)
//            }
        }
    }

    interface ConfirmCallback {
        fun clickNegative()
        fun clickPositive(value: Float)
    }

    private var mCallback: ConfirmCallback? = null

    fun setCallback(callback: ConfirmCallback) {
        mCallback = callback
    }

    fun newInstance(): RatingDialog {
        return RatingDialog()
    }
}
