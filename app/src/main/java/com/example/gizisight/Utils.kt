package com.example.gizisight

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

private val FILENAME_FORMAT = "dd-MMM-yyyy"
val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())


fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

/**
 * Most phone cameras are landscape, meaning if you take the photo in portrait,
 *the resulting photos will be rotated 90 degrees. In this case, the camera software
 * should populate the Exif data with the orientation that the photo should be viewed in.
 */
fun exif(currentPhotoPath: String): Bitmap {
    val ei = ExifInterface(currentPhotoPath)
    val orientation: Int = ei.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_UNDEFINED
    )
    var rotatedBitmap: Bitmap?
    when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotatedBitmap =
            TransformationUtils.rotateImage(BitmapFactory.decodeFile(currentPhotoPath), 90)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotatedBitmap =
            TransformationUtils.rotateImage(BitmapFactory.decodeFile(currentPhotoPath), 180)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotatedBitmap =
            TransformationUtils.rotateImage(BitmapFactory.decodeFile(currentPhotoPath), 270)
        ExifInterface.ORIENTATION_NORMAL -> rotatedBitmap =
            BitmapFactory.decodeFile(currentPhotoPath)
        else -> rotatedBitmap = BitmapFactory.decodeFile(currentPhotoPath)
    }
    return rotatedBitmap
}

fun capitalizeFirstLetterEachWord(input: String?): String {
    return input?.split(" ")!!.joinToString(" ") { it.capitalize() }
}

fun errorJson(msg : String?, activity : Context ) {
    // Check if the errorBody is not null and is a valid JSON
    if (msg != null) {
        try {
            val errorJson = JSONObject(msg)
            val errorMessage = errorJson.getString("message")
            Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
        } catch (e: JSONException) {
            // Handle JSON parsing exception
            Toast.makeText(activity, "Error parsing JSON", Toast.LENGTH_SHORT)
                .show()
            Log.e("REfefee", "Error parsing JSON", e)
        }
    } else {
        // Handle the case where errorBody is null
        Toast.makeText(activity, "Unknown error", Toast.LENGTH_SHORT).show()
    }

    Log.d("REfefee", msg.toString())
}
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


suspend fun reduceFileImage(file: File): File = withContext(Dispatchers.IO) {
    var bitmap = exif(file.path)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > 1000000)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return@withContext file
}