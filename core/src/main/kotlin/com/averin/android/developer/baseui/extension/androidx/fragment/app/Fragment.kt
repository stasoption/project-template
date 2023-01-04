package com.averin.android.developer.baseui.extension.androidx.fragment.app

import android.app.Activity
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.averin.android.developer.baseui.extension.android.content.debugClick
import com.averin.android.developer.baseui.extension.android.content.getColorKtx
import com.averin.android.developer.baseui.extension.android.content.getDrawableKtx
import com.averin.android.developer.baseui.presentation.BackPressable
import com.averin.android.developer.baseui.presentation.bottomsheet.MessageBottomSheet
import com.averin.android.developer.core.R
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

@Suppress("ComplexMethod", "ReturnCount")
inline fun <reified T : Any> Fragment.findCallbackOrNull(checkAllParents: Boolean = true): T? {
    if (targetFragment is T) return targetFragment as T
    var parent = parentFragment
    if (checkAllParents) {
        while (parent != null) {
            if (parent is T) {
                return parent
            } else {
                parent = parent.parentFragment
            }
        }
    } else {
        if (parent is T) return parent
    }
    if (context is T) return context as T
    return null
}

inline fun <reified T : Any> Fragment.findCallback(checkAllParents: Boolean = true): T {
    val callback: T? = findCallbackOrNull(checkAllParents)
    if (callback != null) {
        return callback
    } else {
        throw IllegalStateException("Can't find Callback in targetFragment, parentFragment or context")
    }
}

fun <T : Any> Fragment.getNavigationResult(key: String) =
    findNavController().currentBackStackEntry?.savedStateHandle?.get<T>(key)

fun <T : Any> Fragment.setNavigationResult(key: String, result: T? = null) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

fun <T : Any> Fragment.clearNavigationResult(key: String) {
    findNavController().previousBackStackEntry?.savedStateHandle?.remove<T>(key)
}

fun Fragment.supportFragmentManager() = (requireActivity() as AppCompatActivity).supportFragmentManager

fun Fragment.getColor(@ColorRes id: Int) = requireContext().getColorKtx(id)

fun Fragment.getDrawable(@DrawableRes id: Int) = requireContext().getDrawableKtx(id)

fun Fragment.showMessageDialog(
    message: String,
    @DrawableRes iconId: Int = R.drawable.ic_ok,
    title: String? = null,
    closeDialogListener: (() -> Unit)? = null
) {
    MessageBottomSheet.newInstance(message, iconId, title).apply {
        onCloseDialogListener = closeDialogListener
    }.show(supportFragmentManager(), MessageBottomSheet::class.java.name)
}

fun Fragment.showWarningDialog(
    message: String,
    @DrawableRes iconId: Int = R.drawable.ic_warning_big,
    closeDialogListener: (() -> Unit)? = null
) {
    MessageBottomSheet.newInstance(message, iconId).apply {
        onCloseDialogListener = closeDialogListener
    }.show(supportFragmentManager(), MessageBottomSheet::class.java.name)
}

fun Fragment.showDialogWithAction(
    message: String,
    title: String? = null,
    @DrawableRes iconId: Int = R.drawable.ic_warning_big,
    @StringRes buttonTextId: Int = R.string.btn_ok,
    clickActionListener: (() -> Unit) = { context?.debugClick() },
    closeDialogListener: (() -> Unit)? = null
) {
    MessageBottomSheet.newInstance(
        title = title,
        message = message,
        iconId = iconId
    ).apply {
        onCloseDialogListener = closeDialogListener
        bindAction(buttonTextId, clickActionListener)
    }.show(supportFragmentManager(), MessageBottomSheet::class.java.name)
}

fun Fragment.hideKeyBoard() {
    val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = requireActivity().currentFocus ?: View(requireActivity())
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.showKeyboard() {
    val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = requireActivity().currentFocus ?: View(requireActivity())
    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun Fragment.setKeyboardEventListener(listener: (isOpen: Boolean) -> Unit) {
    KeyboardVisibilityEvent.setEventListener(requireActivity(), viewLifecycleOwner) { isOpen ->
        listener.invoke(isOpen)
    }
}

fun Fragment.onBackPressed(owner: LifecycleOwner, action: () -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(
        owner,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() = action()
        }
    )
}

fun Fragment.onBackPressed(action: () -> Unit) {
    onBackPressed(viewLifecycleOwner, action)
}

fun Fragment.parentFragmentOnBackPressed() = (parentFragment as? BackPressable)?.onBackPressed()

fun Fragment.postDelayed(delayMillis: Long, runnable: Runnable) {
    Handler().postDelayed(runnable, delayMillis)
}

fun Fragment.post(runnable: Runnable) {
    Handler().post(runnable)
}
