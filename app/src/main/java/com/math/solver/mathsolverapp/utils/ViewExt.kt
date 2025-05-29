package com.math.solver.mathsolverapp.utils

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.math.solver.mathsolverapp.R

fun View.goneView() {
    visibility = View.GONE
}

fun View.visibleView() {
    visibility = View.VISIBLE
}

fun View.invisibleView() {
    visibility = View.INVISIBLE
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun View.click(context: Context, onClick: () -> Unit) {
    val animation = AnimationUtils.loadAnimation(context, R.anim.view_push)
    setOnClickListener {
        this.startAnimation(animation)
        onClick()
    }
}
fun View.clickOnce(threshold: Long = 500L, action: () -> Unit) {
    setOnClickListener {
        isEnabled = false
        action.invoke()
        postDelayed({
            isEnabled = true
        }, threshold)
    }
}
fun <T> Context.openActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}

fun View.setVisible() {
    visibility = View.VISIBLE
}

fun View.setInVisible() {
    visibility = View.INVISIBLE
}

fun View.setGone() {
    visibility = View.GONE
}

fun <T> Context.openActivityWithClearTask(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
}


@SuppressLint("ClickableViewAccessibility")
fun View.rippleEffect(color: String) {
    setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                v.foreground =
                    RippleDrawable( //Using the foreground allows you to give the view whatever background you need
                        ColorStateList.valueOf(Color.parseColor(color)),
                        null,//Whatever shape you put here will cover everything you've got underneath so you probably want to keep it "null"
                        v.background
                    )
                v.foreground.setHotspot(event.x, event.y)
            }
        }
        false
    }
    isClickable = true
}

fun View.expand(heightTarget: Int) {
    clearAnimation()
    Handler(Looper.getMainLooper()).postDelayed({
        if (!isVisible) visibleView() else invisibleView()
    }, 80)
    val valueAnimator = ValueAnimator.ofInt(0, heightTarget)
    valueAnimator.addUpdateListener { animation ->
        layoutParams.height = animation.animatedValue as Int
        requestLayout()
    }
    valueAnimator.interpolator = DecelerateInterpolator()
    valueAnimator.duration = 800
    valueAnimator.start()
}

fun View.collapse(heightTarget: Int) {
    val prevHeight = height
    clearAnimation()
    val valueAnimator = ValueAnimator.ofInt(prevHeight, heightTarget)
    valueAnimator.interpolator = DecelerateInterpolator()
    valueAnimator.addUpdateListener { animation ->
        layoutParams.height = animation.animatedValue as Int
        if (animation.animatedValue as Int == 0) invisibleView()
        requestLayout()
    }
    valueAnimator.interpolator = DecelerateInterpolator()
    valueAnimator.duration = 170
    valueAnimator.start()
}

fun expandView(v: View, durationMs: Int, targetHeightDp: Int) {
    val prevHeight = v.height
    var inProcess = false
    val valueAnimator =
        ValueAnimator.ofInt(prevHeight, dpToPx(targetHeightDp.toFloat()))
    valueAnimator.addUpdateListener { animation ->
        if (animation.animatedValue as Int > 0 && !inProcess) {
            inProcess = true
            v.visibility = View.VISIBLE
        }
        v.layoutParams.height = animation.animatedValue as Int
        v.requestLayout()
    }
    valueAnimator.interpolator = DecelerateInterpolator()
    valueAnimator.duration = durationMs.toLong()
    valueAnimator.start()
}

fun collapseView(v: View, durationMs: Int, targetHeightDp: Int) {
    val prevHeight = v.height
    val valueAnimator =
        ValueAnimator.ofInt(prevHeight, dpToPx(targetHeightDp.toFloat()))
    valueAnimator.interpolator = DecelerateInterpolator()
    valueAnimator.addUpdateListener { animation ->
        v.layoutParams.height = animation.animatedValue as Int
        if (animation.animatedValue as Int == 0) v.visibility = View.GONE
        v.requestLayout()
    }
    valueAnimator.interpolator = DecelerateInterpolator()
    valueAnimator.duration = durationMs.toLong()
    valueAnimator.start()
}

fun dpToPx(dp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        Resources.getSystem().displayMetrics
    )
        .toInt()
}


inline fun <reified T> Context.openActivityWithBlock(block: Intent.() -> Unit) {
    val intent = Intent(this, T::class.java)
    intent.block()
    startActivity(intent)
}

fun <T> Context.openActivityWithAction(
    it: Class<T>, action: String? = null, extras: Bundle.() -> Unit = {}
) {
    val intent = Intent(this, it).apply {
        action?.let {
            this.action = it
        }
        putExtras(Bundle().apply(extras))
    }
    startActivity(intent)
}

fun <T> Context.openActivityAndClearApp(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}

fun <T> Context.openActivityClearTop(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}

fun View.setSafeOnClickListener(interval: Long = 2000, onSafeClick: (View) -> Unit) {
    var lastClickTime: Long = 0
    setOnClickListener { v ->
        val clickTime = SystemClock.elapsedRealtime()
        if (clickTime - lastClickTime < interval) {
            return@setOnClickListener
        }
        onSafeClick(v)
        lastClickTime = clickTime
    }
}

fun Activity.transparentStatusBar() {
    window.setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
}

fun Activity.hideNavigationBar() {
    if (Build.VERSION.SDK_INT in Build.VERSION_CODES.R..Build.VERSION_CODES.TIRAMISU) {
        window.insetsController?.let { controller ->
            controller.hide(WindowInsets.Type.navigationBars())
            controller.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }
}

fun View.tap(delay: Long? = 300L, action: (view: View?) -> Unit) {
    setOnClickListener(object : TapListener(delay) {
        override fun onTap(v: View?) {
            action(v)
        }
    })
}

abstract class TapListener(private var delay: Long? = 300L) : View.OnClickListener {
    private var lastClickMillis: Long = 0
    private var now = 0L
    override fun onClick(v: View?) {
        now = SystemClock.elapsedRealtime()
        if (now - lastClickMillis > (delay ?: 300L))
            onTap(v)
        lastClickMillis = now
    }

    abstract fun onTap(v: View?)
}

fun View.slideUp(duration: Long = 200, endAction: (() -> Unit)? = null) {
    this.animate()
        .translationY(-this.height.toFloat())
        .alpha(0f)
        .setDuration(duration)
        .withEndAction {
            this.visibility = View.GONE
            endAction?.invoke()
        }
}

fun View.slideDown(duration: Long = 200, endAction: (() -> Unit)? = null) {
    this.visibility = View.VISIBLE
    this.animate()
        .translationY(0f)
        .alpha(1f)
        .setDuration(duration)
        .withEndAction {
            endAction?.invoke()
        }
}

fun showDialogSafely(fragmentManager: FragmentManager, dialog: DialogFragment, tag: String) {
    val existingDialog = fragmentManager.findFragmentByTag(tag) as? DialogFragment
    if (!fragmentManager.isStateSaved) {
        existingDialog?.dismissAllowingStateLoss()
        dialog.show(fragmentManager, tag)
    } else {
        Log.e("scp", "Cannot show dialog: FragmentManager is in a saved state or destroyed")
    }
}

fun dismissDialogSafely(fragmentManager: FragmentManager, tag: String) {
    val existingDialog = fragmentManager.findFragmentByTag(tag) as? DialogFragment
    if (existingDialog != null) {
        existingDialog.dismissAllowingStateLoss()
    } else {
        Log.w("scp", "No dialog found with tag: $tag to dismiss.")
    }
}

fun AppCompatActivity.replaceFragmentInActivity(
    fragment: Fragment,
    frameId: Int,
    isAddToBackStack: Boolean = false,
    isPopBackStack: Boolean = false,
    tag: String? = null,
) {
    try {
        if (isPopBackStack)
            supportFragmentManager.fragments.let {
                for (i in (it.size - 1) downTo 0)
                    supportFragmentManager.beginTransaction().remove(it[i]).commit()
            }

        supportFragmentManager.beginTransaction().apply {
            if (isAddToBackStack)
                add(frameId, fragment, tag ?: fragment.javaClass.simpleName)
            else
                replace(frameId, fragment, tag ?: fragment.javaClass.simpleName)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            if (isAddToBackStack) {
                addToBackStack(fragment.javaClass.simpleName)
            }
            commit()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}



fun View.show() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}

fun View.hide() {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
}

fun View.showOrHide(show: Boolean) {
    if (show) show() else hide()
}

fun View.gone(isGone: Boolean = true) {
    visibility = if (isGone) View.GONE else View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun clearFocusFromRecyclerView(recyclerView: RecyclerView) {
    // Loop through all children (visible items) in the RecyclerView and clear focus
    for (i in 0 until recyclerView.childCount) {
        val child = recyclerView.getChildAt(i)
        child.clearFocus()
    }
}

