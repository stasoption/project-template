package com.averin.android.developer.baseui.extension.android.view

import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import com.averin.android.developer.base.NO_VALUE_INT
import com.averin.android.developer.baseui.util.GlideApp
import com.averin.android.developer.baseui.util.GlideOptions.bitmapTransform
import com.averin.android.developer.baseui.util.GlideRequest
import com.averin.android.developer.core.R
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import jp.wasabeef.glide.transformations.MaskTransformation

fun ImageView.setNavigationIcon(id: Int) {
    val drawableMainAction: Drawable? = if (id != NO_VALUE_INT) {
        AppCompatResources.getDrawable(context, id)
    } else {
        null
    }
    setImageDrawable(drawableMainAction)
    isVisible = drawableMainAction != null
}

fun ImageView.loadAvatar(
    imageUrl: String?,
    @DrawableRes mask: Int = R.drawable.ic_round_mask,
    progressView: View? = null,
) {

    if (imageUrl.isNullOrEmpty()) {
        loadDrawable(
            R.drawable.ic_avatar_placeholder,
            R.drawable.ic_round_mask
        )
        return
    }
    loadImage(
        imageUrl = imageUrl,
        mask = mask,
        progressView = progressView
    )
}

fun ImageView.loadLogotype(
    imageUrl: String?,
    @DrawableRes mask: Int = R.drawable.ic_round_mask,
    @DrawableRes placeholder: Int = R.drawable.ic_group_placeholder,
    progressView: View? = null,
) {
    if (imageUrl.isNullOrEmpty()) {
        loadDrawable(placeholder, mask)
        return
    }
    loadImage(
        imageUrl = imageUrl,
        mask = mask,
        progressView = progressView
    )
}

fun ImageView.loadImage(
    imageUrl: String?,
    @DrawableRes mask: Int? = null,
    @DrawableRes placeholder: Int? = R.drawable.image_placeholder,
    bitmapTransformation: BitmapTransformation = CenterCrop(),
    progressView: View? = null,
    errorBlock: ((e: GlideException) -> Unit)? = null
) {
    if (imageUrl.isNullOrEmpty()) {
        loadDrawable(
            placeholder ?: R.drawable.image_placeholder,
            mask ?: R.drawable.ic_round_mask
        )
        return
    }
    progressView?.isVisible = true
    val glideRequest: GlideRequest<Drawable> = GlideApp.with(context)
        .load(imageUrl)
        .apply(
            RequestOptions()
                .override(this.width * 2, this.height * 2)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
        ).listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any,
                target: Target<Drawable>,
                isFirstResource: Boolean
            ): Boolean {
                progressView?.isVisible = false
                e?.let { errorBlock?.invoke(it) }
                Handler().post {
                    loadDrawable(
                        placeholder ?: R.drawable.image_placeholder,
                        mask ?: R.drawable.ic_round_mask
                    )
                }
                return false
            }
            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable>,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                progressView?.isVisible = false
                return false
            }
        })
    mask?.let {
        glideRequest.apply(
            bitmapTransform(
                MultiTransformation(
                    bitmapTransformation,
                    MaskTransformation(it)
                )
            )
        )
    }
    glideRequest.into(this)
}

fun ImageView.loadDrawable(
    @DrawableRes drawableRes: Int,
    @DrawableRes mask: Int,
    bitmapTransformation: BitmapTransformation = CenterCrop()
) {
    GlideApp.with(context)
        .load(drawableRes)
        .apply(
            RequestOptions()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .dontAnimate()
        )
        .apply(
            bitmapTransform(
                MultiTransformation(
                    bitmapTransformation,
                    MaskTransformation(mask)
                )
            )
        )
        .into(this)
}

fun ImageView.loadUri(
    uri: Uri,
    @DrawableRes mask: Int? = R.drawable.ic_round_mask,
    bitmapTransformation: BitmapTransformation = CenterCrop(),
    @DrawableRes placeholder: Int? = R.drawable.image_placeholder,
    progressView: View? = null
) {
    progressView?.isVisible = true
    val glideRequest: GlideRequest<Bitmap> = GlideApp.with(context)
        .asBitmap()
        .load(uri)
        .apply(
            RequestOptions()
                .error(placeholder ?: R.drawable.image_placeholder)
                .placeholder(placeholder ?: R.drawable.image_placeholder)
        ).listener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {
                progressView?.isVisible = false
                return false
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                progressView?.isVisible = false
                return false
            }
        })
    mask?.let {
        glideRequest.apply(
            bitmapTransform(
                MultiTransformation(
                    bitmapTransformation,
                    MaskTransformation(it)
                )
            )
        )
    }
    glideRequest.into(this)
}

fun ImageView.setTintColor(color: Int) {
    colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
}
