package com.averin.android.developer.baseui.widget.recycler.decoration

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.averin.android.developer.core.R

open class DefaultHorizontalSpacingItemDecoration(
    context: Context,
    spacingPredicate: (item: Any?) -> Boolean = { true }
) : SpacingItemDecoration(
    betweenItems = context.resources.getDimensionPixelOffset(R.dimen.margin_small),
    paddingLeft = context.resources.getDimensionPixelOffset(R.dimen.margin_medium),
    paddingRight = context.resources.getDimensionPixelOffset(R.dimen.margin_medium),
    orientation = RecyclerView.HORIZONTAL,
    spacingPredicate = spacingPredicate
)
