package com.averin.android.developer.baseui.extension.android.graphics

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

const val JPEG_QUALITY = 80

fun Drawable.toBitmap(): Bitmap? {
    if (this is BitmapDrawable) {
        return this.bitmap
    }
    val bitmap = Bitmap.createBitmap(this.intrinsicWidth, this.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    this.setBounds(0, 0, canvas.width, canvas.height)
    this.draw(canvas)
    return bitmap
}

/**
 * After creationg of new bitmap the old one will be recycled [Bitmap.recycle]
 */
fun Bitmap.scaleBitmap(@IntRange(from = 1) newHeight: Int, @IntRange(from = 1) newWidth: Int): Bitmap {
    if (width == newWidth && height == newHeight) {
        return this
    }
    synchronized(this::class.java) {
        val dest: Bitmap
        val config = if (config == null) Bitmap.Config.ARGB_8888 else config
        dest = Bitmap.createBitmap(newWidth, newHeight, config)
        val canvas = Canvas(dest)
        val xScale = newWidth.toFloat() / width
        val yScale = newHeight.toFloat() / height
        val scale = max(xScale, yScale)

        val scaledWidth = scale * width
        val scaledHeight = scale * height

        val dst = RectF(0f, 0f, scaledWidth, scaledHeight)
        canvas.drawBitmap(this, null, dst, null)

        recycle()
        return dest
    }
}

fun Bitmap.scaleCenterCrop(newHeight: Int, newWidth: Int): Bitmap {
    val xScale = newWidth.toFloat() / width
    val yScale = newHeight.toFloat() / height
    val scale = max(xScale, yScale)

    val scaledWidth = scale * width
    val scaledHeight = scale * height

    val left = (newWidth - scaledWidth) / 2
    val top = (newHeight - scaledHeight) / 2

    val targetRect = RectF(left, top, left + scaledWidth, top + scaledHeight)

    var dest: Bitmap
    synchronized(this::class.java) {
        dest = Bitmap.createBitmap(newWidth, newHeight, config)
        val canvas = Canvas(dest)
        canvas.drawBitmap(this, null, targetRect, null)
        recycle()
        return dest
    }
}

@Synchronized
fun Bitmap.getRounded(@FloatRange(from = 1.0) radius: Float): Bitmap {
    val output = roundedWithOutRecycledSource(radius)
    recycle()
    return output
}

fun Bitmap.roundedWithOutRecycledSource(@FloatRange(from = 1.0) radius: Float): Bitmap {
    val width = width
    val height = height

    val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)

    val shader = BitmapShader(this, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

    val paint = Paint()
    paint.isAntiAlias = true
    paint.shader = shader

    val rect = RectF(0.0f, 0.0f, width.toFloat(), height.toFloat())
    canvas.drawRoundRect(rect, radius, radius, paint)
    return output
}

@Synchronized
fun Bitmap.circled(): Bitmap {
    val size = min(width, height)
    val dst = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    circledWithOutRecycled(dst, size)
    recycle()
    return dst
}

fun Bitmap.circledWithOutRecycled(dst: Bitmap, size: Int) {
    dst.setHasAlpha(true)

    val canvas = Canvas(dst)

    val color = -0xbdbdbe
    val paint = Paint()
    val rect = Rect(0, 0, size, size)

    canvas.drawColor(Color.TRANSPARENT)
    paint.isAntiAlias = true
    canvas.drawARGB(0, 0, 0, 0)
    paint.color = color

    val bounds = size.toFloat() / 2

    canvas.drawCircle(bounds, bounds, bounds, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, rect, rect, paint)
}

@Synchronized
fun Uri.scaleImageFile(newWidth: Int, newHeight: Int): Bitmap? {
    if (!isFileUri()) {
        return null
    }

    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(path, options)
    options.inSampleSize = calculateInSampleSize(options, newWidth, newHeight)
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeFile(path, options)
}

fun Uri?.isFileUri(): Boolean {
    return this != null && "file".equals(scheme, ignoreCase = true)
}

private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}

fun Bitmap.compressToFile(filePath: String) {
    try {
        val file = File(filePath)
        val outputStream = FileOutputStream(file)
        compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, outputStream)
        outputStream.close()
    } catch (e: IOException) {
        Timber.e(e, "Compress bitmap error")
    }
}

@Synchronized
fun Resources.decodeResourceAsRgb565Bitmap(resourceId: Int): Bitmap {
    val opts = BitmapFactory.Options()
    opts.inPreferredConfig = Bitmap.Config.RGB_565
    return BitmapFactory.decodeResource(this, resourceId, opts)
}

/**
 * @param config если null то используется [Bitmap.Config.ARGB_8888]
 */
fun Bitmap.mutate(config: Bitmap.Config?): Bitmap? {
    if (isMutable) {
        return this
    }

    synchronized(this::class.java) {
        val copy = copy(config ?: Bitmap.Config.ARGB_8888, true) ?: return null
        if (copy != this) {
            recycle()
        }
        return copy
    }
}

fun Bitmap.blur(@IntRange(from = 1) radius: Int): Bitmap? {
    val blurredBitmap = mutate(Bitmap.Config.ARGB_8888) ?: return null
    return fastBlur(radius)
}

// fast blur for all versions if needed
@Suppress("LongMethod", "ComplexMethod", "MagicNumber")
private fun Bitmap.fastBlur(radius: Int): Bitmap? {
    // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>
    if (radius < 1) {
        return null
    }

    val pix = IntArray(width * height)
    getPixels(pix, 0, width, 0, 0, width, height)

    val wm = width - 1
    val hm = height - 1
    val wh = width * height
    val div = radius + radius + 1

    val r = IntArray(wh)
    val g = IntArray(wh)
    val b = IntArray(wh)
    var rsum: Int
    var gsum: Int
    var bsum: Int
    var x: Int
    var y: Int
    var i: Int
    var p: Int
    var yp: Int
    var yi: Int
    var yw: Int
    val vmin = IntArray(max(width, height))

    var divsum = div + 1 shr 1
    divsum *= divsum
    val dv = IntArray(256 * divsum)
    i = 0
    while (i < 256 * divsum) {
        dv[i] = i / divsum
        i++
    }

    yi = 0
    yw = yi

    val stack = Array(div) { IntArray(3) }
    var stackpointer: Int
    var stackstart: Int
    var sir: IntArray
    var rbs: Int
    val r1 = radius + 1
    var routsum: Int
    var goutsum: Int
    var boutsum: Int
    var rinsum: Int
    var ginsum: Int
    var binsum: Int

    y = 0
    while (y < height) {
        bsum = 0
        gsum = bsum
        rsum = gsum
        boutsum = rsum
        goutsum = boutsum
        routsum = goutsum
        binsum = routsum
        ginsum = binsum
        rinsum = ginsum
        i = -radius
        while (i <= radius) {
            p = pix[yi + min(wm, max(i, 0))]
            sir = stack[i + radius]
            sir[0] = p and 0xff0000 shr 16
            sir[1] = p and 0x00ff00 shr 8
            sir[2] = p and 0x0000ff
            rbs = r1 - abs(i)
            rsum += sir[0] * rbs
            gsum += sir[1] * rbs
            bsum += sir[2] * rbs
            if (i > 0) {
                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]
            } else {
                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]
            }
            i++
        }
        stackpointer = radius

        x = 0
        while (x < width) {

            r[yi] = dv[rsum]
            g[yi] = dv[gsum]
            b[yi] = dv[bsum]

            rsum -= routsum
            gsum -= goutsum
            bsum -= boutsum

            stackstart = stackpointer - radius + div
            sir = stack[stackstart % div]

            routsum -= sir[0]
            goutsum -= sir[1]
            boutsum -= sir[2]

            if (y == 0) {
                vmin[x] = min(x + radius + 1, wm)
            }
            p = pix[yw + vmin[x]]

            sir[0] = p and 0xff0000 shr 16
            sir[1] = p and 0x00ff00 shr 8
            sir[2] = p and 0x0000ff

            rinsum += sir[0]
            ginsum += sir[1]
            binsum += sir[2]

            rsum += rinsum
            gsum += ginsum
            bsum += binsum

            stackpointer = (stackpointer + 1) % div
            sir = stack[stackpointer % div]

            routsum += sir[0]
            goutsum += sir[1]
            boutsum += sir[2]

            rinsum -= sir[0]
            ginsum -= sir[1]
            binsum -= sir[2]

            yi++
            x++
        }
        yw += width
        y++
    }
    x = 0
    while (x < width) {
        bsum = 0
        gsum = bsum
        rsum = gsum
        boutsum = rsum
        goutsum = boutsum
        routsum = goutsum
        binsum = routsum
        ginsum = binsum
        rinsum = ginsum
        yp = -radius * width
        i = -radius
        while (i <= radius) {
            yi = max(0, yp) + x

            sir = stack[i + radius]

            sir[0] = r[yi]
            sir[1] = g[yi]
            sir[2] = b[yi]

            rbs = r1 - abs(i)

            rsum += r[yi] * rbs
            gsum += g[yi] * rbs
            bsum += b[yi] * rbs

            if (i > 0) {
                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]
            } else {
                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]
            }

            if (i < hm) {
                yp += width
            }
            i++
        }
        yi = x
        stackpointer = radius
        y = 0
        while (y < height) {
            // Preserve alpha channel: ( 0xff000000 & pix[yi] )
            pix[yi] = -0x1000000 and pix[yi] or (dv[rsum] shl 16) or (dv[gsum] shl 8) or dv[bsum]

            rsum -= routsum
            gsum -= goutsum
            bsum -= boutsum

            stackstart = stackpointer - radius + div
            sir = stack[stackstart % div]

            routsum -= sir[0]
            goutsum -= sir[1]
            boutsum -= sir[2]

            if (x == 0) {
                vmin[y] = min(y + r1, hm) * width
            }
            p = x + vmin[y]

            sir[0] = r[p]
            sir[1] = g[p]
            sir[2] = b[p]

            rinsum += sir[0]
            ginsum += sir[1]
            binsum += sir[2]

            rsum += rinsum
            gsum += ginsum
            bsum += binsum

            stackpointer = (stackpointer + 1) % div
            sir = stack[stackpointer]

            routsum += sir[0]
            goutsum += sir[1]
            boutsum += sir[2]

            rinsum -= sir[0]
            ginsum -= sir[1]
            binsum -= sir[2]

            yi += width
            y++
        }
        x++
    }

    setPixels(pix, 0, width, 0, 0, width, height)

    return this
}
