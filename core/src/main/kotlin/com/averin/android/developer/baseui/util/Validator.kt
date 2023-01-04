package com.averin.android.developer.baseui.util

import java.util.regex.Pattern

const val MAX_TEXT_LENGTH: Int = 5000
const val MAX_SMS_LENGTH: Int = 6
const val MIN_PHONE_LENGTH: Int = 8
const val VERIFICATION_CODE_LENGTH: Int = 6

val emailPattern: Pattern = Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
        "\\@" +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
        "(" +
        "\\." +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
        ")+"
)

fun String.validateNotEmpty(): Boolean = this.isNotEmpty()

fun String.validatePhone(): Boolean = this.isNotEmpty() && this.length > MIN_PHONE_LENGTH

fun String.validateSms(): Boolean = this.isNotEmpty() && this.length == MAX_SMS_LENGTH

fun String.validateMail(): Boolean = this.isNotEmpty() && emailPattern.matcher(this.trim()).matches()

fun String.validatePassword(): Boolean = this.isNotEmpty()
