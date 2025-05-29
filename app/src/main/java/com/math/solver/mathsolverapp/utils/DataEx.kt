package com.math.solver.mathsolverapp.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.math.solver.mathsolverapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Matcher
import java.util.regex.Pattern

class ListLiveData<T> : MutableLiveData<MutableList<T>>()

fun <T> LiveData<T>.hasValue(): Boolean {
    return value?.let {
        true
    } ?: kotlin.run {
        false
    }
}

//fun <T> Flow<T>.trackingProgress(progressBar: PublishSubject<Boolean>): Flow<T> {
//    return this.onStart {
//        printLog("onStart")
//        progressBar.onNext(true)
//    }.onCompletion {
//        printLog("onCompletion")
//        progressBar.onNext(false)
//    }
//}


fun <T> CoroutineScope.executeAsync(
    onPreExecute: suspend () -> Unit,
    doInBackground: suspend () -> T,
    onPostExecute: (T) -> Unit
) = launch {
    onPreExecute()
    val result = withContext(Dispatchers.IO) {
        doInBackground()
    }
    onPostExecute(result)
}

fun isNetwork(activity: Activity): Boolean {
    val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
}

fun logEvent(context: Context, event: String) {
//    FirebaseAnalytics.getInstance(context).logEvent(event, Bundle())
}

fun hideKeyboard(activity: Activity) {
    val inputMethodManager =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}

@SuppressLint("ObsoleteSdkInt")
fun setStatusBar(activity: Activity, @DrawableRes statusBar: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = activity.window
        if (window != null) {
            @SuppressLint("UseCompatLoadingForDrawables") val background: Drawable =
                activity.resources.getDrawable(statusBar)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = activity.resources.getColor(android.R.color.transparent)
            window.setBackgroundDrawable(background)
        }
    }
}


@SuppressLint("ObsoleteSdkInt", "ResourceType")
fun setStatusBarColor(activity: Activity, @ColorInt statusBar: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = activity.window
        if (window != null) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = activity.resources.getColor(statusBar)
        }
    }
}

fun getWithMetrics(activity: Activity): Int {
    val displayMetrics = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}

fun getHeightMetrics(activity: Activity): Int {
    val displayMetrics = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.heightPixels
}

@SuppressLint("UseCompatLoadingForDrawables")
fun getIconEffect(context: Context, fileName: String): Int {
    var icon = -1
    if (fileName.contains("normal")) {
//        icon = R.drawable.ic_normal_effect
    }
//    if (fileName.contains("robot")) {
//        icon = R.drawable.ic_robot_effect
//    }
//    if (fileName.contains("helium")) {
//        icon = R.drawable.ic_helium_effect
//    }
//    if (fileName.contains("hexafluoride")) {
//        icon = R.drawable.ic_hexafluoride_effect
//    }
//    if (fileName.contains("cave")) {
//        icon = R.drawable.ic_cave_effect
//    }
//    if (fileName.contains("drunk")) {
//        icon = R.drawable.ic_drunk__effect
//    }
//    if (fileName.contains("bigobot")) {
//        icon = R.drawable.ic_big_robot_effect
//    }
//    if (fileName.contains("extraterrestrial")) {
//        icon = R.drawable.ic_extraterrestrial_effect
//    }
//    if (fileName.contains("monster")) {
//        icon = R.drawable.ic_monster_effect
//    }
//    if (fileName.contains("nervous")) {
//        icon = R.drawable.ic_nervous_effect
//    }
//    if (fileName.contains("telephone")) {
//        icon = R.drawable.ic_telephone_effect
//    }
//    if (fileName.contains("backchipmunk")) {
//        icon = R.drawable.ic_back_chipmunks_effect
//    }
//    if (fileName.contains("underwater")) {
//        icon = R.drawable.ic_underwater_effect
//    }
//    if (fileName.contains("zombie")) {
//        icon = R.drawable.ic_zombie_effect
//    }
//    if (fileName.contains("child")) {
//        icon = R.drawable.ic_child_effect
//    }
//    if (fileName.contains("megaphone")) {
//        icon = R.drawable.ic_megaphone_effect
//    }
//    if (fileName.contains("death")) {
//        icon = R.drawable.ic_death_effect
//    }
//    if (fileName.contains("villain")) {
//        icon = R.drawable.ic_villain_effect
//    }
//    if (fileName.contains("alien")) {
//        icon = R.drawable.ic_alien_effect
//    }
//    if (fileName.contains("smallalien")) {
//        icon = R.drawable.ic_small_alien_effect
//    }
//    if (fileName.contains("squirrel")) {
//        icon = R.drawable.ic_squirrel_effect
//    }
    return icon
}

fun getCurrencySub(currency: String): String {
    var result = ""
    val number = "0123456789.,"
    if (number.contains(currency[0])) {
        for (c in currency) {
            if (!number.contains(c)) {
                val index = currency.indexOf(c)
                result = currency.substring(index, currency.length)
                break
            }
        }
    } else {
        for (c in currency) {
            if (number.contains(c)) {
                val index = currency.indexOf(c)
                result = currency.substring(0, index)
                break
            }
        }
    }
    return result
}

fun checkFistLastCurrency(currency: String): Boolean {
    val number = "0123456789"
    if (number.contains(currency[0])) {
        return true
    }
    return false
}

fun round2Dec(number: Float): String? {
    return String.format("%.2f", number)
}

fun getSpecialCharacterCount(s: String?): Boolean {
    val p: Pattern = Pattern.compile("[^A-Za-z0-9_ ]")
    val m: Matcher = p.matcher(s)
    val b: Boolean = m.find()
    if (b) {
        return true
    }
    return false
}
