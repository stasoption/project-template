package com.averin.android.developer.base.extension.java.io

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.DatabaseUtils
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import androidx.exifinterface.media.ExifInterface
import com.averin.android.developer.base.extension.kotlin.removeNoDigits
import com.averin.android.developer.base.util.CountingRequestBody
import com.averin.android.developer.base.util.CustomFileProvider
import com.averin.android.developer.baseui.widget.imagepicker.ImagePreviewBottomSheet
import com.averin.android.developer.core.BuildConfig
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.*
import java.net.URLDecoder
import java.text.DecimalFormat

private const val EOF = -1
private const val DOCUMENTS_DIR = "documents"
private const val BYTES = " B"
private const val KILOBYTES = " KB"
private const val MEGABYTES = " MB"
private const val GIGABYTES = " GB"
const val BYTES_IN_KILOBYTES = 1024
const val BYTES_IN_MEGABYTES = 1_048_576
const val MAX_FILE_SIZE_MB = 15 * BYTES_IN_KILOBYTES
const val MAX_IMAGE_SIZE_MB = 2 * BYTES_IN_MEGABYTES

private const val GOOGLE_PHOTOS_PACKAGE_NAME = "com.google.android.apps.photos"
private const val GOOGLE_PLUS_PACKAGE_NAME = "com.google.android.apps.plus"
private const val GOOGLE_DOCUMENTSUI_PACKAGE_NAME = "com.android.documentsui"
private const val GOOGLE_DOCS_PACKAGE_NAME = "com.google.android.apps.docs"
private const val BROWSER_PACKAGE_NAME = "com.android.browser"

/**
 * Gets the extension of a file name, like ".png" or ".jpg".
 * @param uri
 * @return Extension including the dot("."); "" if there is no extension;
 * null if uri was null.
 */
val String?.extractExtension: String
    get() {
        this ?: return ""
        val dot = this.lastIndexOf(".")
        return if (dot >= 0) {
            this.substring(dot)
        } else {
            // No extension.
            ""
        }
    }

/**
 * Get the file size in a human-readable string.
 *
 * @param size
 * @return
 * @author paulburke
 */
val Long.readableFileSize: String
    get() {
        val dec = DecimalFormat("###.#")
        var fileSize = 0f
        var suffix = KILOBYTES
        if (this > BYTES_IN_KILOBYTES) {
            fileSize = (this / BYTES_IN_KILOBYTES).toFloat()
            if (fileSize > BYTES_IN_KILOBYTES) {
                fileSize /= BYTES_IN_KILOBYTES
                if (fileSize > BYTES_IN_KILOBYTES) {
                    fileSize /= BYTES_IN_KILOBYTES
                    suffix = GIGABYTES
                } else {
                    suffix = MEGABYTES
                }
            }
        } else {
            fileSize = this.toFloat()
            suffix = BYTES
        }
        return dec.format(fileSize.toDouble()) + suffix
    }

/**
 *
 * @param path file path (locale or url)
 * @return path type
 */
val String.isUrl: Boolean
    get() {
        val s = this.trim { it <= ' ' }.lowercase()
        return s.startsWith("http://") || s.startsWith("https://")
    }

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is local.
 */
fun Uri.isLocalStorageDocument(context: Context): Boolean {
    return CustomFileProvider.getAuthority(context) == this.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is ExternalStorageProvider.
 */
val Uri.isExternalStorageDocument: Boolean
    get() = "com.android.externalstorage.documents" == this.authority

/**
 * @param uri
 * The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 */
val Uri.isDownloadsDocument: Boolean
    get() = "com.android.providers.downloads.documents" == this.authority

/**
 * @param uri
 * The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 */
val Uri.isMediaDocument: Boolean
    get() = "com.android.providers.media.documents" == this.authority

/**
 * @param uri
 * The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 */
val Uri.isGooglePhotosUri: Boolean
    get() = "com.google.android.apps.photos.content" == this.authority

val Uri.isGoogleDriveUri: Boolean
    get() = "com.google.android.apps.docs.storage.legacy" == this.authority ||
        "com.google.android.apps.docs.storage" == this.authority

fun File.toUri(context: Context): Uri =
    CustomFileProvider.getUriForFile(context, this)

fun File.toBase64(): String = Base64.encodeToString(readBytes(), Base64.NO_WRAP)

fun String.readBytesFromFilePath(): ByteArray? {
    var fileInputStream: FileInputStream? = null
    var bytesArray: ByteArray? = null
    try {
        val file = File(this)
        bytesArray = ByteArray(file.length().toInt())

        // read file into bytes[]
        fileInputStream = FileInputStream(file)
        fileInputStream.read(bytesArray)
    } catch (e: IOException) {
        Timber.e(e)
    } finally {
        if (fileInputStream != null) {
            try {
                fileInputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    return bytesArray
}

fun File.toPutRequestBody(
    onLoadingProgressListener: ((percentage: Float) -> Unit)? = null
): RequestBody? {
    val buf1 = this.path.readBytesFromFilePath()
    val mameType = this.getMimeType()
    val body = buf1?.toRequestBody(mameType.toMediaTypeOrNull())
    body ?: return null
    val countingBody = CountingRequestBody(body) { bytesWritten, contentLength ->
        val percentage: Float = 100f * bytesWritten / contentLength
        onLoadingProgressListener?.invoke(percentage)
    }
    return countingBody
}

/**
 * @return The MIME type for the given file.
 */
fun File?.getMimeType(): String {
    val defaultType = "application/octet-stream"
    this ?: return defaultType
    val extension = this.name.extractExtension
    return if (extension.isNotEmpty()) {
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1)) ?: defaultType
    } else {
        defaultType
    }
}

/**
 * @return The MIME type for the give Uri.
 */
fun Uri?.getMimeType(context: Context): String? {
    this ?: return null
    val file = File(this.getFilePath(context))
    return file.getMimeType()
}

/**
 * @return The MIME type for the give String Uri.
 */
fun String?.getMimeType(context: Context): String? {
    this ?: return null
    var type = context.contentResolver.getType(Uri.parse(this))
    if (type == null) {
        type = "application/octet-stream"
    }
    return type
}

fun isForbiddenPackage(packageName: String): Boolean {
    return packageName.contains(GOOGLE_PHOTOS_PACKAGE_NAME) ||
        packageName.contains(GOOGLE_PLUS_PACKAGE_NAME) ||
        packageName.contains(GOOGLE_DOCUMENTSUI_PACKAGE_NAME) ||
        packageName.contains(GOOGLE_DOCS_PACKAGE_NAME) ||
        packageName.contains(BROWSER_PACKAGE_NAME)
}

/**
 * Creates View intent for given file
 *
 * @param file
 * @return The intent for viewing file
 */
fun File.getViewIntent(context: Context): Intent {
    val uri = CustomFileProvider.getUriForFile(context, this)
    val intent = Intent(Intent.ACTION_VIEW)
    val url = this.toString()
    if (url.contains(".doc") || url.contains(".docx")) {
        // Word document
        intent.setDataAndType(uri, "application/msword")
    } else if (url.contains(".pdf")) {
        // PDF file
        intent.setDataAndType(uri, "application/pdf")
    } else if (url.contains(".ppt") || url.contains(".pptx")) {
        // Powerpoint file
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
    } else if (url.contains(".xls") || url.contains(".xlsx")) {
        // Excel file
        intent.setDataAndType(uri, "application/vnd.ms-excel")
    } else if (url.contains(".zip") || url.contains(".rar")) {
        // WAV audio file
        intent.setDataAndType(uri, "application/x-wav")
    } else if (url.contains(".rtf")) {
        // RTF file
        intent.setDataAndType(uri, "application/rtf")
    } else if (url.contains(".wav") || url.contains(".mp3")) {
        // WAV audio file
        intent.setDataAndType(uri, "audio/x-wav")
    } else if (url.contains(".gif")) {
        // GIF file
        intent.setDataAndType(uri, "image/gif")
    } else if (url.contains(".jpg") || url.contains(".jpeg") || url.contains(".png")) {
        // JPG file
        intent.setDataAndType(uri, "image/jpeg")
    } else if (url.contains(".txt")) {
        // Text file
        intent.setDataAndType(uri, "text/plain")
    } else if (url.contains(".3gp") || url.contains(".mpg") || url.contains(".mpeg") ||
        url.contains(".mpe") || url.contains(".mp4") || url.contains(".avi")
    ) {
        // Video files
        intent.setDataAndType(uri, "video/*")
    } else {
        intent.setDataAndType(uri, "*/*")
    }
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    return intent
}

fun Uri.getFilePath(context: Context): String {
    val absolutePath: String? = getLocalPath(context, this)
    val result = absolutePath ?: this.toString()
    return URLDecoder.decode(result, "UTF-8")
}

@Throws(IOException::class)
private fun FileInputStream.toByteArray(): ByteArray {
    val bytes = ByteArray(channel.size().toInt())
    val buf = BufferedInputStream(this)
    buf.read(bytes, 0, bytes.size)
    buf.close()
    return bytes
}

@Throws(IOException::class)
private fun File.copyExifMetaDataFrom(
    originFileInputStream: InputStream
) {
    val originalFileExif = ExifInterface(originFileInputStream)
    val attributes = arrayOf(ExifInterface.TAG_ORIENTATION)
    val compressedExif = ExifInterface(this.absolutePath)
    for (attribute in attributes) {
        val value: String? = originalFileExif.getAttribute(attribute)
        value?.let { compressedExif.setAttribute(attribute, value) }
    }
    compressedExif.saveAttributes()
}

@SuppressLint("BinaryOperationInTimber")
private fun getLocalPath(context: Context, uri: Uri): String? {
    // DocumentProvider
    if (DocumentsContract.isDocumentUri(context, uri)) {
        // LocalStorageProvider
        if (uri.isLocalStorageDocument(context)) {
            // The path is the id
            return DocumentsContract.getDocumentId(uri)
        } else if (uri.isExternalStorageDocument) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":").toTypedArray()
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            } else if ("home".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/documents/" + split[1]
            }
        } else if (uri.isDownloadsDocument) {
            val id = DocumentsContract.getDocumentId(uri)
            if (id != null && id.startsWith("raw:")) {
                return id.substring(4)
            }
            val contentUriPrefixesToTry = arrayOf(
                "content://downloads/public_downloads",
                "content://downloads/my_downloads"
            )
            for (contentUriPrefix: String in contentUriPrefixesToTry) {
                val parsedId: String = id.removeNoDigits()?.trim() ?: ""
                val contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), parsedId.toLong())
                try {
                    val path: String? = getDataColumn(
                        context,
                        contentUri,
                        null,
                        null
                    )
                    if (path != null) {
                        return path
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }

            // path could not be retrieved using ContentResolver, therefore copy file to accessible cache using streams
            val fileName: String = getFileName(context, uri)
            val cacheDir: File = getDocumentCacheDir(context)
            val file: File? = generateFileWithName(fileName, cacheDir)
            var destinationPath: String? = null

            if (file != null) {
                destinationPath = file.absolutePath
                saveFileFromUri(context, uri, destinationPath)
            }
            return destinationPath
        } else if (uri.isMediaDocument) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":").toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null
            when (type) {
                "image" -> {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
                "video" -> {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                }
                "audio" -> {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
            }
            val selection = "_id=?"
            val selectionArgs = arrayOf(
                split[1]
            )
            val path = contentUri?.let {
                getDataColumn(
                    context,
                    it,
                    selection,
                    selectionArgs
                )
            }
            return if (path != null) {
                path
            } else {
                val fileName: String = getFileName(context, uri)
                val cacheDir: File = getDocumentCacheDir(context)
                val file: File? = generateFileWithName(fileName, cacheDir)
                var destinationPath: String? = null

                if (file != null) {
                    destinationPath = file.absolutePath
                    saveFileFromUri(context, uri, destinationPath)
                }
                return destinationPath
            }
        } else if (uri.isGoogleDriveUri) {
            return getGoogleDriveFilePath(uri, context)
        }
    } else if ("content".equals(uri.scheme, ignoreCase = true)) {
        // Return the remote address
        if (uri.isGooglePhotosUri) {
            return uri.lastPathSegment
        } else if (uri.isGoogleDriveUri) {
            return getGoogleDriveFilePath(uri, context)
        }
        return getDataColumn(context, uri, null, null)
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        return uri.path
    }
    return null
}

/**
 * Get the value of the data column for this Uri. This is useful for
 * MediaStore Uris, and other file-based ContentProviders.
 *
 * @param context
 * The context.
 * @param uri
 * The Uri to query.
 * @param selection
 * (Optional) Filter used in the query.
 * @param selectionArgs
 * (Optional) Selection arguments used in the query.
 * @return The value of the _data column, which is typically a file path.
 */
fun getDataColumn(
    context: Context,
    uri: Uri,
    selection: String?,
    selectionArgs: Array<String>?
): String? {
    var cursor: Cursor? = null
    val column = MediaStore.Files.FileColumns.DATA
    val projection = arrayOf(column)
    try {
        cursor = context.contentResolver.query(
            uri,
            projection,
            selection,
            selectionArgs,
            null
        )
        if (cursor != null && cursor.moveToFirst()) {
            if (BuildConfig.DEBUG) {
                DatabaseUtils.dumpCursor(cursor)
            }
            val columnIndex = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(columnIndex)
        }
    } catch (e: Exception) {
        return null
    } finally {
        cursor?.close()
    }
    return null
}

fun Uri.fromSharedContent(context: Context): File {
    val inputStream = context.contentResolver.openInputStream(this)
    val fileName = getFileName(context, this)
    val splitName = splitFileName(fileName)
    var tempFile = File.createTempFile(splitName[0], splitName[1])
    tempFile = rename(tempFile, fileName)
    tempFile.deleteOnExit()
    var out: FileOutputStream? = null
    try {
        out = FileOutputStream(tempFile)
    } catch (e: FileNotFoundException) {
        Timber.e(e)
    }

    if (inputStream != null) {
        copy(inputStream, out)
        inputStream.close()
    }

    out?.close()
    return tempFile
}

fun Uri?.showFullScreenImageUri(activity: AppCompatActivity) {
    if (this == null) return
    ImagePreviewBottomSheet.loadImageUri(this)
        .show(activity.supportFragmentManager, ImagePreviewBottomSheet::class.java.name)
}

private fun splitFileName(fileName: String): Array<String> {
    var name = fileName
    var extension = ""
    val i = fileName.lastIndexOf(".")
    if (i != EOF) {
        name = fileName.substring(0, i)
        extension = fileName.substring(i)
    }

    return arrayOf(name, extension)
}

@SuppressLint("Range")
private fun getFileName(context: Context, uri: Uri): String {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        try {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        } catch (e: Exception) {
            Timber.e(e)
        } finally {
            cursor?.close()
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result!!.lastIndexOf(File.separator)
        if (cut != EOF) {
            result = result.substring(cut + 1)
        }
    }
    return result.replace("/", "_")
}

private fun getDocumentCacheDir(context: Context): File {
    val dir = File(context.cacheDir, DOCUMENTS_DIR)
    if (!dir.exists()) {
        dir.mkdirs()
    }
    logDir(context.cacheDir)
    logDir(dir)
    return dir
}

private fun logDir(dir: File) {
    if (!BuildConfig.DEBUG) return
    Timber.d("Dir=$dir")
    val files = dir.listFiles()
    files?.forEach { Timber.d("File=${it.path}") }
}

private fun generateFileWithName(name: String?, directory: File): File? {
    if (name == null) {
        return null
    }
    var file = File(directory, name)
    if (file.exists()) {
        var fileName: String = name
        var extension = ""
        val dotIndex = name.lastIndexOf('.')
        if (dotIndex > 0) {
            fileName = name.substring(0, dotIndex)
            extension = name.substring(dotIndex)
        }
        var index = 0
        while (file.exists()) {
            index++
            file = File(directory, "$fileName($index)$extension")
        }
    }
    try {
        if (!file.createNewFile()) {
            return null
        }
    } catch (e: IOException) {
        Timber.e(e)
        return null
    }
    logDir(directory)
    return file
}

private fun saveFileFromUri(context: Context, uri: Uri, destinationPath: String) {
    var inputStream: InputStream? = null
    var bos: BufferedOutputStream? = null
    try {
        inputStream = context.contentResolver.openInputStream(uri)
        bos = BufferedOutputStream(FileOutputStream(destinationPath, false))
        val buf = ByteArray(MAX_FILE_SIZE_MB)
        inputStream?.read(buf)
        do {
            bos.write(buf)
        } while (inputStream?.read(buf) != EOF)
    } catch (e: IOException) {
        Timber.e(e)
    } finally {
        try {
            inputStream?.close()
            bos?.close()
        } catch (e: IOException) {
            Timber.e(e)
        }
    }
}

@SuppressLint("Recycle")
private fun getGoogleDriveFilePath(uri: Uri, context: Context): String? {
    val returnCursor = context.contentResolver.query(uri, null, null, null, null)
    returnCursor ?: return null
    try {
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex).replace("/", "_")
        val dir = File(context.cacheDir, DOCUMENTS_DIR)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        logDir(context.cacheDir)
        logDir(dir)

        val file = File(dir.path, name)
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        var read = 0
        val maxBufferSize = 1 * 1024 * 1024
        val bytesAvailable = inputStream!!.available()
        val bufferSize = Math.min(bytesAvailable, maxBufferSize)
        val buffers = ByteArray(bufferSize)
        while (inputStream.read(buffers).also { read = it } != EOF) {
            outputStream.write(buffers, 0, read)
        }
        inputStream.close()
        outputStream.close()
        return file.path
    } catch (e: Exception) {
        Timber.e(e)
        return null
    }
}

private fun rename(file: File, newName: String): File {
    val newFile = File(file.parent, newName)
    if (newFile != file) {
        if (newFile.exists() && newFile.delete()) {
            Timber.d("Delete old $newName file")
        }
        if (file.renameTo(newFile)) {
            Timber.d("Rename file to $newName")
        }
    }
    return newFile
}

private fun copy(input: InputStream, output: OutputStream?): Long {
    var count: Long = 0
    var n: Int = 0
    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
    while (EOF != n) {
        output!!.write(buffer, 0, n)
        count += n.toLong()
        n = input.read(buffer)
    }
    return count
}
