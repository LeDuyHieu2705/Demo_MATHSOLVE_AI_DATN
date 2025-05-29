package com.hieuld.datn.mathsolved.ui.cropper

import android.content.Context
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.hieuld.datn.mathsolved.R
import com.hieuld.datn.mathsolved.utils.commons.utils.setGone
import com.hieuld.datn.mathsolved.utils.commons.utils.setVisible

internal class AnimationLoadingScan @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {
    private val TAG = "AnimationLoadingScan"


    private var lottieAnimationView: LottieAnimationView? = null


    init {
        initializeLottieAnimationView()
    }

    private fun initializeLottieAnimationView() {
        lottieAnimationView = LottieAnimationView(context).apply {
            // Set the animation from a JSON file in the raw directory
            setAnimation(R.raw.ani_scan_text)
            repeatCount = LottieDrawable.INFINITE
            setGone()
        }
    }

    fun startAnimation() {
        Log.d("sobutag", "startAnimation")
        lottieAnimationView?.let {
            if (it.parent == null) {
                (parent as? ViewGroup)?.addView(it)
            }
            it.setVisible()
            it.playAnimation()
        }
    }

    fun setAnimationToRect(rect: RectF) {
        Log.d(TAG, "setAnimationToRect: $rect")
        lottieAnimationView?.let {
            it.layoutParams = FrameLayout.LayoutParams(rect.width().toInt(), rect.height().toInt()).apply {
                leftMargin = rect.left.toInt()
                topMargin = rect.top.toInt()
            }
        }
    }

    fun stopAnimation() {
        Log.d("sobutag", "stopAnimation")
        lottieAnimationView?.let {
            it.cancelAnimation()
            it.setGone()
        }
    }
}