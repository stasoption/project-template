package com.averin.android.developer.settings.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserInfo(
    @Json(name = "email") val email: String,
    @Json(name = "first_name") val firstName: String?,
    @Json(name = "last_name") val lastName: String?,
    @Json(name = "phone") val phone: String?,
    @Json(name = "img_url") val imgUrl: String?,
    @Json(name = "job_title") val jobTitle: String?,
    @Json(name = "is_owner") val isOwner: Boolean
) {
    val fullName: String
        get() {
            val fullName = "${firstName ?: ""} ${lastName ?: ""}".trim()
            return fullName.ifEmpty { email }
        }
}
