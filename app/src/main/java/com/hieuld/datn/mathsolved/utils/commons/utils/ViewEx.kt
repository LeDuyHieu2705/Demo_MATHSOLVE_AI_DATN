package com.hieuld.datn.mathsolved.utils.commons.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.hieuld.datn.mathsolved.utils.commons.listener.TapListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("NewApi")
fun Activity.changeStatusBarColor(color: Int, lightStatusBar: Boolean = false) {
    this.window?.statusBarColor = resources.getColor(color, theme)
    if (lightStatusBar)
        this.lightStatusBar()

}

fun Activity.lightStatusBar() {
    val decorView: View? = this.window?.decorView
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val wic = decorView?.windowInsetsController
        wic?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    } else
        decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
}

fun View.tap(action: (view: View?) -> Unit) {
    setOnClickListener(object : TapListener() {
        override fun onTap(v: View?) {
            action(v)
        }
    })
}

fun Context.dpToPx(dp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        resources.displayMetrics
    ).toInt()
}


fun View.getLocationOnScreen(): Point {
    val location = IntArray(2)
    this.getLocationOnScreen(location)
    return Point(location[0], location[1])
}

fun View.getViewCenterLocationOnScreen(): Point {
    val location = getLocationOnScreen()
    val centerX = location.x + measuredWidth / 2
    val centerY = location.y + measuredHeight / 2
    return Point(centerX, centerY)
}


fun Context.openWebPage(url: String) {
    val webpage: Uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, webpage)
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    }
}

fun ViewGroup.resizeChildren(newWidth: Int, newHeight: Int) {
    // size is in pixels so make sure you have taken device display density into account
    val childCount = childCount
    for (i in 0 until childCount) {
        val v: View = getChildAt(i)
        if (v is ViewGroup) {
            v.layoutParams = ViewGroup.LayoutParams(newWidth, newHeight)
        }
    }
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

fun View.setSafeOnClickListener(interval: Long = 2000, onSafeClick: (View) -> Unit) {
    var lastClickTime: Long = 0

    setOnClickListener { v ->
        val clickTime = SystemClock.elapsedRealtime()

        if (clickTime - lastClickTime < interval) {
            // Người dùng nhấn quá nhanh, tránh double click
            return@setOnClickListener
        }

        // Thực hiện công việc của bạn ở đây
        onSafeClick(v)
        lastClickTime = clickTime
    }
}

fun Context.isInternetConnected(): Boolean {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}


fun Activity.setWindowFlag(bits: Int, on: Boolean) {
    val win = window
    val winParams = win.attributes
    if (on) {
        winParams.flags = winParams.flags or bits
    } else {
        winParams.flags = winParams.flags and bits.inv()
    }
    win.attributes = winParams
}

fun Activity.hideStatusBar() {
    setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
    window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    if (Build.VERSION.SDK_INT >= 22) {
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = Color.TRANSPARENT
    }
}

fun Activity.showStatusBarAndNavigationBar() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    if (Build.VERSION.SDK_INT >= 30) {
        window.insetsController?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }
    if (Build.VERSION.SDK_INT >= 22) {
        window.statusBarColor = Color.TRANSPARENT
    }
}

fun Activity.hideNavigationBar(activity: Activity) {
    // Ẩn thanh điều hướng (navigation bar)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val controller = window.insetsController
        controller?.let {
            it.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            it.hide(WindowInsets.Type.navigationBars())
        }
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }

}

fun Activity.changeStatusBarColor(color: Int) {
    val decorView = window.decorView

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val controller = decorView.windowInsetsController
        controller?.let {
            it.setSystemBarsAppearance(0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
            window.statusBarColor = color
            it.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, 0)
        }
    } else
        window.statusBarColor = color
    decorView.systemUiVisibility = if (isColorDark(color)) {
        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    } else {
        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}

private fun isColorDark(color: Int): Boolean {
    val darkness: Double =
        1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
    return darkness >= 0.5
}


fun dp2px(dpValue: Float): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}


fun <T> Context.openActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}

fun <T> Context.openActivityWithClearTask(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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


fun Context.setBackground(layout: View, idImage: Int) {
    // Lấy Drawable từ resources bằng cách sử dụng context
    val backgroundDrawable = ContextCompat.getDrawable(this, idImage)

    // Thiết lập background cho LinearLayout
    layout.background = backgroundDrawable
}

fun Context.isServiceRunning(serviceClass: Class<out Service>): Boolean {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
        if (serviceClass.name == service.service.className) {
            return true
        }
    }
    return false
}

fun scrollToView(scrollView: NestedScrollView, view: View) {
    // Đặt một hàm Runnable để đảm bảo cuộn sẽ thực hiện sau khi view đã nhận được focus
    scrollView.post {
        val scrollBounds = Rect()
        scrollView.getHitRect(scrollBounds)
        if (!view.getLocalVisibleRect(scrollBounds)) {
            scrollView.smoothScrollTo(0, view.top - scrollView.height / 2 + view.height / 2)
        }
    }
}

fun Activity.showKeyboardOnView(view: View) {
    view.post {
        view.requestFocus()
        val imgr = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imgr.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        try {
            // Hiển thị bàn phím
            imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun Activity.hideKeyboardEdt() {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    currentFocus?.let {
        inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
}


private val lastToastTimes: MutableMap<String, Long> = HashMap()
fun Context.showToastOnceEvery30Seconds(msg: String) {
    val currentTime = System.currentTimeMillis()

    if (!lastToastTimes.containsKey(msg) || currentTime - lastToastTimes[msg]!! > 30000) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
        }

        lastToastTimes[msg] = currentTime
    }
}

fun Context.showToastOnceEvery15Seconds(msg: String) {
    val currentTime = System.currentTimeMillis()

    if (!lastToastTimes.containsKey(msg) || currentTime - lastToastTimes[msg]!! > 15000) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
        }

        lastToastTimes[msg] = currentTime
    }
}

fun Context.showToastShort(msg: String) {
    CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }
}

fun Context.showToastLong(msg: String) {
    CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
    }
}

fun AppCompatActivity.addOnBackPressedDispatcher(onBackPressed: () -> Unit = { finish() }) {
    onBackPressedDispatcher.addCallback(
        this,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed.invoke()
            }
        }
    )
}


fun setBoldText(view: TextView, boldPart: String, normalPart: String) {
    val fullText = "$boldPart $normalPart"
    val spannableString = SpannableString(fullText)
    spannableString.setSpan(
        StyleSpan(Typeface.BOLD),
        0,
        boldPart.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    view.text = spannableString
}

fun drawableToBitmap(drawable: Drawable): Bitmap {
    if (drawable is BitmapDrawable) {
        return drawable.bitmap
    }

    val width = drawable.intrinsicWidth.takeIf { it > 0 } ?: 1
    val height = drawable.intrinsicHeight.takeIf { it > 0 } ?: 1

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return bitmap
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