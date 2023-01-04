package com.averin.android.developer.baseui.widget.field

interface ValidatableField {
    val isValid: Boolean
    fun validate()
    fun showError(message: String?)
}
