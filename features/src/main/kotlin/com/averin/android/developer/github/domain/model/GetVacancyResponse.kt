package com.averin.android.developer.github.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class GetVacancyResponse(
    @Json(name = "items") val items: List<GitHubProject>
)
