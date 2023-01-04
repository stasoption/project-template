package com.averin.android.developer.github.domain.model

import com.averin.android.developer.baseui.presentation.adapter.AdapterModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GitHubProject(
    @Json(name = "id") val id: Int
) : AdapterModel
