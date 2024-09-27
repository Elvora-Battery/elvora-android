package com.unsoed.elvora.helper

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())
private const val MAXIMAL_SIZE = 1000000


fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}

fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun formatDateString(inputDate: String): String? {
    return try {
        val originalFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = originalFormat.parse(inputDate)

        val targetFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        targetFormat.format(date!!)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun formatDatePlusOneMonth(inputDate: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())

    val date = inputFormat.parse(inputDate)

    val calendar = Calendar.getInstance()
    if (date != null) {
        calendar.time = date
    }
    calendar.add(Calendar.MONTH, 1)

    return outputFormat.format(calendar.time)
}

fun formatDate(inputDate: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())

    val date = inputFormat.parse(inputDate)

    return if (date != null) {
        outputFormat.format(date)
    } else {
        "Invalid date"
    }
}

fun formatNumber(value: Int): String {
    return NumberFormat.getNumberInstance(Locale("id", "ID")).format(value)
}