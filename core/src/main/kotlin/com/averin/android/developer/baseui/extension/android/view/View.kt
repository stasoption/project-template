package com.averin.android.developer.baseui.extension.android.view

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import com.averin.android.developer.base.util.convertFormats
import com.averin.android.developer.base.util.dateNow
import com.averin.android.developer.base.util.fromServerToDateTimeFormat
import com.averin.android.developer.base.util.fromUiToDateTimeFormat
import com.averin.android.developer.base.util.toServerFormat
import com.averin.android.developer.baseui.presentation.dialog.DatePickerDialogFragment
import com.averin.android.developer.baseui.widget.field.CustomSelectView
import org.joda.time.DateTime

private const val BIRTHDAY_RANGE = 18 // years
private const val MIN_DATE_PICKER_DATE = 100 // years

inline fun View.doOnGlobalLayout(crossinline action: (view: View) -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            action(this@doOnGlobalLayout)
        }
    })
}

fun View.doOnApplyWindowInsets(block: (View, insets: WindowInsetsCompat, initialPadding: Rect) -> WindowInsetsCompat) {
    val initialPadding = this.recordInitialPadding()
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        block(v, insets, initialPadding)
    }
    requestApplyInsetsWhenAttached()
}

fun CustomSelectView.showBirthdayDatePicker(
    fragmentManager: FragmentManager,
    onDateSelected: ((dateTime: DateTime) -> Unit)
) {
    val dialogInitialDate: DateTime = this.textValue.fromUiToDateTimeFormat()
        ?: dateNow.minusYears(BIRTHDAY_RANGE)
    DatePickerDialogFragment.show(
        fm = fragmentManager,
        dialogInitialDate = dialogInitialDate,
        dialogMinDate = dateNow.minusYears(MIN_DATE_PICKER_DATE).millis,
        dialogMaxDate = dateNow.millis,
        listener = { onDateSelected.invoke(it) }
    )
}

fun showExperienceDatePicker(
    fragmentManager: FragmentManager,
    initialDate: String,
    onDateSelected: ((dateTime: DateTime) -> Unit)
) {
    DatePickerDialogFragment.show(
        fm = fragmentManager,
        dialogInitialDate = initialDate.fromServerToDateTimeFormat() ?: dateNow,
        dialogMinDate = dateNow.minusYears(100).millis,
        dialogMaxDate = dateNow.millis,
        listener = { onDateSelected.invoke(it) }
    )
}

fun CustomSelectView.showVacancyDatePicker(
    fragmentManager: FragmentManager,
    onDateSelected: ((serverFormatDate: String) -> Unit),
    onDateError: (() -> Unit)? = null
) {
    val dialogInitialDate: DateTime = this.textValue.fromUiToDateTimeFormat() ?: dateNow
    DatePickerDialogFragment.show(
        fm = fragmentManager,
        dialogInitialDate = dialogInitialDate,
        dialogMinDate = dateNow.minusYears(100).millis,
        dialogMaxDate = dateNow.plusYears(100).millis,
        listener = { dateTime ->
            dateTime.toServerFormat()?.let { selectedDate ->
                onDateSelected.invoke(selectedDate)
            }
            dateTime.convertFormats()?.let { date ->
                textValue = date
            } ?: run {
                onDateError?.invoke()
            }
        }
    )
}

fun View.getDrawable(@DrawableRes id: Int): Drawable = context.resources.getDrawable(id, null)

fun View.getColor(@ColorRes id: Int): Int = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
    context.resources.getColor(id, null)
} else {
    context.resources.getColor(id)
}

fun View.setIsEnabled(isEnabled: Boolean) {
    this.isEnabled = isEnabled
    this.alpha = if (isEnabled) {
        1F
    } else {
        0.4F
    }
}

fun View.setBackgroundTintColor(color: Int) {
    this.background.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
}

private fun View.recordInitialPadding() =
    Rect(this.paddingLeft, this.paddingTop, this.paddingRight, this.paddingBottom)

private fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        ViewCompat.requestApplyInsets(this)
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                ViewCompat.requestApplyInsets(v)
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}
