package com.averin.android.developer.baseui.widget.tag

import com.averin.android.developer.baseui.presentation.adapter.AdapterModel
import com.averin.android.developer.core.R

class Tag(
    val id: Int,
    val title: String
) : AdapterModel {
    var isSelected: Boolean = false

    private val tagColorsResources = listOf(
        Pair(R.color.tag_bg_color_1, R.color.tag_txt_color_1),
        Pair(R.color.tag_bg_color_2, R.color.tag_txt_color_2),
        Pair(R.color.tag_bg_color_3, R.color.tag_txt_color_3),
        Pair(R.color.tag_bg_color_4, R.color.tag_txt_color_4)
    )

    fun tagColorsResourcePair(): Pair<Int, Int> {
        val step = tagColorsResources.size
        val num = id * 31
        val rest = num % step
        return if (rest < tagColorsResources.size) {
            tagColorsResources[rest]
        } else {
            tagColorsResources.first()
        }
    }
}
