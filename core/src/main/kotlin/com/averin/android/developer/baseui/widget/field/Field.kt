package com.averin.android.developer.baseui.widget.field

import androidx.annotation.StringDef

@Retention(AnnotationRetention.SOURCE)
@StringDef(
    Field.EMAIL,
    Field.NEW_PASSWORD
)
annotation class Field {
    companion object {
        const val EMAIL = "email"
        const val NEW_PASSWORD = "new_password"
    }
}
