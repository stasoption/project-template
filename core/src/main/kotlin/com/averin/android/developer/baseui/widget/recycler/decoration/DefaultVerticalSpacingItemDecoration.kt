package com.averin.android.developer.baseui.widget.recycler.decoration

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.averin.android.developer.core.R

open class DefaultVerticalSpacingItemDecoration(
    context: Context,
    betweenItems: Int = context.resources.getDimensionPixelOffset(R.dimen.margin_4),
    paddingTop: Int = 0,
    paddingBottom: Int = 0,
    spacingPredicate: (item: Any?) -> Boolean = { true }
) : SpacingItemDecoration(
    betweenItems = betweenItems,
    paddingTop = paddingTop,
    paddingBottom = paddingBottom,
    orientation = RecyclerView.VERTICAL,
    spacingPredicate = spacingPredicate
)
