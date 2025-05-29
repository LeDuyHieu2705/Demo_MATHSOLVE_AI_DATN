package com.hieuld.datn.mathsolved.utils.commons.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.hieuld.datn.mathsolved.R
import com.hieuld.datn.mathsolved.di.DIFileComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.security.SecureRandom


fun Context.shareApp() {
    val sendIntent = Intent(Intent.ACTION_SEND)
    sendIntent.putExtra(
        Intent.EXTRA_TEXT,
        String.format(
            "https://play.google.com/store/apps/details?id=%s",
            packageName
        )
    )
    sendIntent.type = "text/plain"
    startActivity(Intent.createChooser(sendIntent, getString(R.string.share_to)))
}

fun Context.openPrivacy(url: String?) {
    if (url.isNullOrEmpty()) return
    try {
        val i = Intent(Intent.ACTION_VIEW)
        if (url.startsWith("http://") || url.startsWith("https://")) {
            i.data = Uri.parse(url)
        } else i.data = Uri.parse("http://$url")
        startActivity(i)
    } catch (ignored: Exception) {
    }
}

fun isEmailValid(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isPasswordValid(password: String): Boolean {
    // Kiểm tra độ dài mật khẩu có ít nhất 6 ký tự
    return password.length >= 6
    // Bổ sung các điều kiện khác tùy theo yêu cầu (ví dụ: ít nhất một chữ hoa, chữ thường, số, ký tự đặc biệt, v.v.)
    // Ở đây, chỉ kiểm tra độ dài
}



fun createCustomItineraryTemplate(template: String, message: String): String {
    return if (template.contains("______")) {
        template.replace("______", "\n$message")  // Thêm ký tự xuống dòng trước khi thêm message
    } else {
        "$template\n$message"  // Thêm ký tự xuống dòng trước khi thêm message
    }
}

fun downloadImage(urlString: String): Bitmap? {
    var bitmap: Bitmap? = null
    try {
        val url = URL(urlString)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        val inputStream = connection.inputStream
        bitmap = BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return bitmap
}

fun resizeBitmap(bitmap: Bitmap, maxWidth: Int): Bitmap {
    val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
    val width = maxWidth
    val height = (maxWidth / aspectRatio).toInt()
    return Bitmap.createScaledBitmap(bitmap, width, height, true)
}

fun bitmapToBase64(bitmap: Bitmap): String {
    val resizedBitmap = resizeBitmap(bitmap, 512)
    val byteArrayOutputStream = ByteArrayOutputStream()
    resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun Context.uriToBitmap(uri: Uri): Bitmap {
    try {
        contentResolver.openInputStream(uri)?.use { inputStream ->
            // Tạo Options để đọc thông tin của ảnh mà không phải đọc toàn bộ
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(inputStream, null, options)

            val maxWidth = 1024

            // Tính toán giảm mẫu dựa trên chiều rộng ảnh và maxWidth
            options.inSampleSize = calculateInSampleSize(options, maxWidth)

            // Tải ảnh với giảm mẫu đã tính toán
            options.inJustDecodeBounds = false
            contentResolver.openInputStream(uri)?.use { newInputStream ->
                return BitmapFactory.decodeStream(newInputStream, null, options)
                    ?: Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return Bitmap.createBitmap(2048, 2048, Bitmap.Config.ARGB_8888)
}

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int): Int {
    val width = options.outWidth
    var inSampleSize = 1

    if (width > reqWidth) {
        val halfWidth = width / 2
        while ((halfWidth / inSampleSize) >= reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}


// Hàm tải ảnh từ URL và chuyển đổi thành Bitmap
fun downloadBitmapFromUrl1(urlString: String, callback: (Bitmap?) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val url = URL(urlString)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(inputStream)
            withContext(Dispatchers.Main) {
                callback(bitmap)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                callback(null)
            }
        }
    }
}


fun Context.rateApp() {
    val packageName = packageName
    try {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=${packageName}")
            )
        )
    } catch (e: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=${packageName}")
            )
        )
    }
}

fun Activity.sendEmail(content: String) {
    val addresses = arrayOf("help@gmail.com")
    val subject = "Feed back " + applicationContext.getString(R.string.app_name) + ""
    val body =
        "Tell us which issues you are facing using " + applicationContext.getString(R.string.app_name) + " App : $content"
    try {
        /*val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        emailIntent.type = "plain/text"
        emailIntent.setClassName(
            "com.google.android.gm",
            "com.google.android.gm.ComposeActivityGmail"
        )
//        emailIntent.setPackage("com.google.android.gm");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, addresses);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);*/

        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "message/rfc822"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, addresses)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, body)
        emailIntent.setPackage("com.google.android.gm");

        startActivity(emailIntent)
    } catch (e: Exception) {
        e.printStackTrace()


        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:" + addresses[0])
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, body)
        try {
            startActivity(
                Intent.createChooser(
                    emailIntent,
                    getString(R.string.mes_send_email_using)
                )
            )
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                this,
                getString(R.string.mes_no_email_clients_installed),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    /*val addresses = arrayOf("
    val subject = "Feed back " + applicationContext.getString(R.string.app_name)
    val body = "Tell us which issues you are facing using " + applicationContext.getString(R.string.app_name) + " App?"

    val emailIntent = Intent(Intent.ACTION_SENDTO)
    emailIntent.data = Uri.parse("mailto:" + Uri.encode(addresses.joinToString(",")))
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
    emailIntent.putExtra(Intent.EXTRA_TEXT, body)

    try {
        startActivity(Intent.createChooser(emailIntent, getString(R.string.mes_send_email_using)))
    } catch (ex: ActivityNotFoundException) {
        Toast.makeText(
            this,
            getString(R.string.mes_no_email_clients_installed),
            Toast.LENGTH_SHORT
        ).show()
    }*/
}

fun generateCodeVerifier(number: Int): String {
    val alphanumeric = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    val secureRandom = SecureRandom()
    val codeVerifier = (1..number)
        .map { alphanumeric[secureRandom.nextInt(alphanumeric.size)] }
        .joinToString("")

    return codeVerifier
}

private val diFileComponent by lazy { DIFileComponent() }



fun isServiceRunningEx(context: Context, serviceClass: Class<*>): Boolean {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
        if (serviceClass.name == service.service.className) {
            return true
        }
    }
    return false
}

fun Context.getRealPathFromURI(uri: Uri): String? {
    var filePath: String? = null
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = contentResolver.query(uri, projection, null, null, null)

    cursor?.use {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            filePath = it.getString(columnIndex)
        }
    }
    return filePath
}

fun Context.getFileFromUri(uri: Uri): File? {
    val cursor = contentResolver.query(uri, arrayOf(MediaStore.Images.Media.DATA), null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val index = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val filePath = it.getString(index)
            return File(filePath)
        }
    }
    return null
}

fun Context.getFileFromUri2(uri: Uri): File? {
    // Kiểm tra nếu uri có thể được chuyển đổi thành file từ MediaStore
    if (uri.scheme == "content") {
        val projection = arrayOf(MediaStore.Files.FileColumns.DATA)
        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
                val filePath = cursor.getString(columnIndex)
                return File(filePath)
            }
        }
    } else if (uri.scheme == "file") {
        // Nếu URI là kiểu "file", bạn có thể tạo đối tượng File trực tiếp
        return File(uri.path!!)
    }
    return null
}

fun Context.getFile(documentUri: Uri): File {
    val inputStream = contentResolver?.openInputStream(documentUri)

    // Lấy tên file gốc từ documentUri
    val fileName = getFileNameFromUri(documentUri) ?: "file_pdf"

    // Tách phần tên và phần mở rộng của file
    val fileExtension = if (fileName.contains(".")) {
        fileName.substring(fileName.lastIndexOf("."))
    } else {
        ".pdf"
    }
    val baseName = fileName.substring(0, fileName.lastIndexOf("."))

    // Tạo tên file mới với tên file gốc + thời gian hiện tại trước phần mở rộng
    val newFileName = "${baseName}_${System.currentTimeMillis()}$fileExtension"

    var file = File("")
    inputStream.use { input ->
        file = File(cacheDir, newFileName)
        FileOutputStream(file).use { output ->
            val buffer = ByteArray(4 * 1024) // or other buffer size
            var read: Int = -1
            while (input?.read(buffer).also {
                    if (it != null) {
                        read = it
                    }
                } != -1) {
                output.write(buffer, 0, read)
            }
            output.flush()
        }
    }
    return file
}

fun Context.getFileNameFromUri(uri: Uri): String? {
    var name: String? = null
    val cursor = contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1) {
                name = it.getString(nameIndex)
            }
        }
    }
    return name
}

fun Context.clearCache() {
    try {
        val cacheDir = cacheDir
        if (cacheDir.isDirectory) {
            cacheDir.deleteRecursively()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


fun isValidUrl(url: String): Boolean {
    return try {
        val uri = Uri.parse(url)
        uri.scheme?.let { scheme ->
            (scheme.equals("http", true) || scheme.equals("https", true)) && uri.host != null
        } ?: false
    } catch (e: Exception) {
        false
    }
}

fun normalizeUrl(url: String): String {
    return try {
        val uri = Uri.parse(url)
        val host = uri.host?.replaceFirst("m.", "") ?: return url
        val newUri = uri.buildUpon().authority(host).build()
        newUri.toString()
    } catch (e: Exception) {
        url
    }
}


@Throws(IOException::class)
fun Context.from(uri: Uri): File {
    try {
        //val uri2 = ContentUris.withAppendedId(uri, 0)
        val inputStream = contentResolver.openInputStream(uri)
        val fileName = getFileName(uri)
        val splitName = splitFileName(fileName)
        var tempFile = File.createTempFile(splitName[0] ?: "", splitName[1])
        tempFile = rename(tempFile, fileName)
        tempFile.deleteOnExit()
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(tempFile)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        if (inputStream != null) {
            copy(inputStream, out)
            inputStream.close()
        }
        out?.close()
        return tempFile
    } catch (e: Exception) {
        return File("")
    }
}

private fun splitFileName(fileName: String?): Array<String?> {
    var name = fileName
    var extension: String? = ""
    val i = fileName!!.lastIndexOf(".")
    if (i != -1) {
        name = fileName.substring(0, i)
        extension = fileName.substring(i)
    }
    return arrayOf(name, extension)
}

fun Context.getFileName(uri: Uri): String? {
    var fileName: String? = null
    val scheme = uri.scheme ?: return null
    if (scheme == "file") {
        return uri.lastPathSegment
    } else if (scheme == "content") {
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            if (cursor.count != 0) {
                val columnIndex =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                fileName = cursor.getString(columnIndex)
            }
            cursor.close()
        }
    }
    return fileName
}

fun rename(file: File, newName: String?): File {
    val newFile = File(file.parent, newName ?: "")
    if (newFile != file) {
        if (newFile.exists() && newFile.delete()) {
            Log.d("FileUtil", "Delete old $newName file")
        }
        if (file.renameTo(newFile)) {
            Log.d("FileUtil", "Rename file to $newName")
        }
    }
    return newFile
}

const val EOF = -1

@Throws(IOException::class)
private fun copy(input: InputStream, output: OutputStream?): Long {
    var count: Long = 0
    var n: Int
    val buffer =
        ByteArray(DEFAULT_BUFFER_SIZE)
    while (EOF != input.read(buffer)
            .also { n = it }
    ) {
        output!!.write(buffer, 0, n)
        count += n.toLong()
    }
    return count
}