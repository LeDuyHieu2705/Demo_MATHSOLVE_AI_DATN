package com.hieuld.datn.mathsolved.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.KeyguardManager
import android.content.ActivityNotFoundException
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.PowerManager
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.hieuld.datn.mathsolved.R
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/*fun Activity.checkShowUpdate(onRemind: () -> Unit, onUpdate: () -> Unit): Boolean {
    try {
        val lastVer: String = instance.appConfigs.lastVersion
        if (lastVer == BuildConfig.VERSION_NAME) return false
        val forceUpdate: Boolean = instance.appConfigs.isForceUpdate
        val showUpdate: Boolean = instance.appConfigs.isShowUpdate
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(getString(R.string.new_update_available))
        if (!forceUpdate) {
            if (showUpdate) {
                alertDialogBuilder
                    .setMessage(getString(R.string.update_for_new_features_and_best_performance))
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.update)) { dialog: DialogInterface, _: Int ->
                        onUpdate.invoke()
                        gotoStore(false)
                        dialog.dismiss()
                    }
                    .setNegativeButton(
                        getString(R.string.remind)
                    ) { dialog: DialogInterface, _: Int ->
                        onRemind.invoke()
                        dialog.dismiss()
                    }
                    .setOnCancelListener {
                        onRemind.invoke()
                    }
                val alertDialog = alertDialogBuilder.create()
                if (!isFinishing || !isDestroyed) {
                    alertDialog.show()
                    return true
                }
            }
        } else {
            alertDialogBuilder
                .setMessage(getString(R.string.update_for_new_features_and_best_performance))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.update)) { dialog: DialogInterface, _: Int ->
                    onUpdate.invoke()
                    gotoStore(false)
                    dialog.dismiss()
                }
            val alertDialog = alertDialogBuilder.create()
            if (!isFinishing || !isDestroyed) {
                alertDialog.show()
                return true
            }
        }
    } catch (ignored: Exception) {
        return false
    }
    return false
}*/

const val NOTIFICATION_PERMISSION = "android.permission.POST_NOTIFICATIONS"
const val NOTIFICATION_PERMISSION_CODE = 11255
const val CHANNEL_NOTIFICATION_DEFAULT = "tape_measure_notification_channel"
fun Context.hasNotificationPermission(): Boolean {
    return (ContextCompat.checkSelfPermission(this, NOTIFICATION_PERMISSION)
            == PackageManager.PERMISSION_GRANTED)
}

fun Activity.requestNotificationPermission() {
    ActivityCompat.requestPermissions(
        this,
        arrayOf(NOTIFICATION_PERMISSION),
        NOTIFICATION_PERMISSION_CODE
    )
}

@Suppress("DEPRECATION")
fun getKeyHash(context: Context): String? {
    val packageInfo: PackageInfo
    var key: String? = null
    try {
        //getting application package name, as defined in manifest
        val packageName = context.applicationContext.packageName
        //Retrieving package info

        packageInfo = context.packageManager.getPackageInfo(
            packageName,
            PackageManager.GET_SIGNATURES
        )
        println("Package Name: " + context.applicationContext.packageName)
        for (signature in packageInfo.signatures!!) {
            val md = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            key = String(Base64.encode(md.digest(), 0))
            println("Key Hash: $key")
        }
    } catch (e1: PackageManager.NameNotFoundException) {
        println("Name not found$e1")
    } catch (e: NoSuchAlgorithmException) {
        println("No such an algorithm$e")
    } catch (e: Exception) {
        println("Exception$e")
    }
    return key
}

/*suspend fun getAdvertisingId(context: Context): String? {
    return withContext(Dispatchers.Default) {
        try {
            val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context)
            adInfo.id
        } catch (exception: Exception) {
            null // Handle the exception and return null if there's an issue
        }
    }
}*/

@SuppressLint("HardwareIds")
fun getDeviceID(context: Context): String? {
    return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}

/*fun shareApp(context: Context) {
    val sendIntent = Intent(Intent.ACTION_SEND)
    sendIntent.putExtra(
        Intent.EXTRA_TEXT,
        instance.appConfigs.shareText + "\n"
                + String.format(
            "https://play.google.com/store/apps/details?id=%s",
            context.packageName
        )
    )
    sendIntent.type = "text/plain"
    context.startActivity(Intent.createChooser(sendIntent, "Share to"))
}*/

/*fun sendEmail(context: Context) {
    val uriText =
        """mailto:${instance.appConfigs.emailSupport}?subject=Rate ${instance.appConfigs.shareText} """.trimIndent()
    val uri = Uri.parse(uriText)
    val sendIntent = Intent(Intent.ACTION_SENDTO)
    sendIntent.data = uri
    context.startActivity(
        Intent.createChooser(
            sendIntent,
            context.getString(R.string.send_email)
        )
    )
}*/

fun openDeeplink(context: Context, url: String?) {
    if (url.isNullOrEmpty()) return
    try {
        val i = Intent(Intent.ACTION_VIEW)
        if (url.startsWith("http://") || url.startsWith("https://")) {
            i.data = Uri.parse(url)
        } else i.data = Uri.parse("http://$url")
        context.startActivity(i)
    } catch (ignored: java.lang.Exception) {
    }
}

/*fun showMoreApp(context: Context) {
    try {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(instance.appConfigs.moreAppURL)
            )
        )
    } catch (e: Exception) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(instance.appConfigs.moreAppURL)
            )
        )
    }
}*/

fun Context.gotoStore(ignoreAd: Boolean) {
//    ignoreOpenAd = ignoreAd
    try {
        this.startActivity(rateIntentForUrl(this, "market://details"))
    } catch (e: ActivityNotFoundException) {
        this.startActivity(
            rateIntentForUrl(
                this,
                "https://play.google.com/store/apps/details"
            )
        )
    }
}

private fun rateIntentForUrl(context: Context, url: String?): Intent {
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(String.format("%s?id=%s", url, context.packageName))
    )
    var flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
    flags = flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
    intent.addFlags(flags)
    return intent
}

/*fun getTokenFirebaseMessage() {
    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.d("scp", "Fetching FCM registration token failed: ${task.exception}")
            return@OnCompleteListener
        }
        // Get new FCM registration token
        val token = task.result
        Log.d("scp", "token firebase: $token")
    })
}*/

fun isPhoneInUse(context: Context?): Boolean {
    context ?: return false

    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

    return powerManager.isInteractive && !keyguardManager.isKeyguardLocked
}

fun getSmsApps(context: Context): List<String> {
    val smsApps = mutableListOf<String>()
    val pm = context.packageManager
    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"))
    val resolveInfoList = pm.queryIntentActivities(intent, 0)

    for (resolveInfo in resolveInfoList) {
        smsApps.add(resolveInfo.activityInfo.packageName)
    }

    return smsApps
}


fun formatDurationAndSize(durationMs: Long, sizeBytes: Long): String {
    // Format thời gian (ms → mm:ss)
    val seconds = durationMs / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    val timeFormatted = String.format("%02d:%02d", minutes, remainingSeconds)

    // Format size (bytes → MB, với 1MB = 1024 * 1024)
    val sizeInMb = sizeBytes.toDouble() / (1024 * 1024)
    val sizeFormatted = if (sizeInMb < 1) {
        String.format("%.1fKB", sizeBytes.toDouble() / 1024)
    } else {
        String.format("%.1fMB", sizeInMb)
    }

    return try {
        "$timeFormatted  $sizeFormatted"
    } catch (e: Exception) {
        ""
    }
}

fun formatFileSize(sizeInBytes: String): String {
    val size = sizeInBytes.toLongOrNull() ?: return "Invalid Size"  // Chuyển size từ String sang Long
    val kb = 1024
    val mb = kb * 1024
    val gb = mb * 1024

    return when {
        size >= gb -> String.format("%.2f GB", size / gb.toFloat())
        size >= mb -> String.format("%.2f MB", size / mb.toFloat())
        size >= kb -> String.format("%.2f KB", size / kb.toFloat())
        else -> String.format("%d Bytes", size)
    }
}


fun Context.onRingtoneButtonClick(filePath: String) {
    val file = File(filePath)
    if (!file.exists()) {
        showToast(getString(R.string.file_does_not_exist))
        return
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // Android 10 trở lên – sử dụng MediaStore
        setRingtoneAndroidQAndAbove(this, file)
    } else {
        // Android dưới Q
        setRingtoneLegacy(this, file)
    }
}


private fun setRingtoneLegacy(context: Context, file: File) {
    val values = ContentValues().apply {
        put(MediaStore.MediaColumns.DATA, file.absolutePath)
        put(MediaStore.MediaColumns.TITLE, file.nameWithoutExtension)
        put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3")
        put(MediaStore.Audio.Media.IS_RINGTONE, true)
        put(MediaStore.Audio.Media.IS_ALARM, false)
        put(MediaStore.Audio.Media.IS_NOTIFICATION, false)
        put(MediaStore.Audio.Media.IS_MUSIC, false)
    }

    val uri = MediaStore.Audio.Media.getContentUriForPath(file.absolutePath)
    val newUri = context.contentResolver.insert(uri!!, values)

    RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri)
    context.showToast(context.getString(R.string.set_as_ringtone))
}

private fun setRingtoneAndroidQAndAbove(context: Context, file: File) {
    val values = ContentValues().apply {
        put(MediaStore.Audio.Media.RELATIVE_PATH, Environment.DIRECTORY_RINGTONES)
        put(MediaStore.Audio.Media.TITLE, file.nameWithoutExtension)
        put(MediaStore.Audio.Media.DISPLAY_NAME, file.name)
        put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp3")
        put(MediaStore.Audio.Media.IS_RINGTONE, true)
    }

    val resolver = context.contentResolver
    val collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    val uri = resolver.insert(collection, values)

    uri?.let {
        resolver.openOutputStream(it)?.use { output ->
            file.inputStream().copyTo(output)
        }

        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, it)
        context.showToast(context.getString(R.string.set_as_ringtone))
    } ?: context.showToast(context.getString(R.string.cannot_set_ringtone))
}


fun Context.onSetAlarm(path: String) {
    val file = File(path)

    if (!file.exists()) {
        showToast(getString(R.string.file_does_not_exist))
        return
    }

    // Kiểm tra quyền chỉnh sửa hệ thống (Android 6+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(this)) {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
            data = Uri.parse("package:$packageName")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        showToast(getString(R.string.please_grant_system_edit_permission_to_set_alarm))
        return
    }

    try {
        val uri = MediaStore.Audio.Media.getContentUriForPath(file.absolutePath) ?: run {
            showToast(getString(R.string.unable_to_access_mediastore))
            return
        }

        val contentResolver = contentResolver

        // Kiểm tra xem file đã có trong MediaStore chưa
        val cursor = contentResolver.query(
            uri,
            arrayOf(MediaStore.Audio.Media._ID),
            MediaStore.MediaColumns.DATA + "=?",
            arrayOf(file.absolutePath),
            null
        )

        val existingUri = if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
            ContentUris.withAppendedId(uri, id)
        } else {
            // Chưa có thì insert mới
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DATA, file.absolutePath)
                put(MediaStore.MediaColumns.TITLE, file.nameWithoutExtension)
                put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3")
                put(MediaStore.Audio.Media.IS_RINGTONE, false)
                put(MediaStore.Audio.Media.IS_NOTIFICATION, false)
                put(MediaStore.Audio.Media.IS_ALARM, true)
                put(MediaStore.Audio.Media.IS_MUSIC, false)
            }
            contentResolver.insert(uri, values)
        }

        cursor?.close()

        if (existingUri != null) {
            RingtoneManager.setActualDefaultRingtoneUri(
                this,
                RingtoneManager.TYPE_ALARM,
                existingUri
            )
            showToast(getString(R.string.set_as_alarm))
        } else {
            showToast(getString(R.string.cannot_set_as_alarm))
        }

    } catch (e: Exception) {
        e.printStackTrace()
        showToast(getString(R.string.error_setting_as_alarm))
    }
}


fun Context.shareAudioFile(path: String) {
    val file = File(path)
    if (!file.exists()) {
        showToast(getString(R.string.file_does_not_exist))
        return
    }

    val uri: Uri = FileProvider.getUriForFile(
        this,
        "${packageName}.fileprovider",
        file
    )

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "audio/*"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    startActivity(Intent.createChooser(shareIntent, getString(R.string.share_audio_via)))
}


fun getEffects(context: Context): String? {
    val json: String = try {
        val `is` = context.assets.open("effects.json")
        val size = `is`.available()
        val buffer = ByteArray(size)
        `is`.read(buffer)
        `is`.close()
        String(buffer, StandardCharsets.UTF_8)
    } catch (ex: IOException) {
        ex.printStackTrace()
        return null
    }
    return json
}
