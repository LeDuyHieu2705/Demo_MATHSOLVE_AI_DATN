package com.math.solver.mathsolverapp.ui.cropper

import android.content.Context
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.math.solver.mathsolverapp.R
import com.math.solver.mathsolverapp.utils.setGone
import com.math.solver.mathsolverapp.utils.setVisible

internal class ScanAnimationLoading @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {


    private var lottieAnimationView: LottieAnimationView? = null

    init {
        initializeLottieAnimationView()
    }

    private fun initializeLottieAnimationView() {
        lottieAnimationView = LottieAnimationView(context).apply {
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

    fun setAnimationToRect(rect: RectF, width: Float) {
        Log.d("sobutag", "setAnimationToRect: $rect, width: $width")
        lottieAnimationView?.let {
            val layoutParams =
                FrameLayout.LayoutParams(rect.width().toInt(), rect.height().toInt()).apply {
                    leftMargin = rect.left.toInt()
                    topMargin = rect.top.toInt()
                }
            it.layoutParams = layoutParams
            it.requestLayout()
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