package com.averin.android.developer.baseui.widget.recycler.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.averin.android.developer.baseui.extension.android.content.getDimensionKtx
import com.averin.android.developer.baseui.extension.android.content.getDrawableKtx
import com.averin.android.developer.core.R
import com.google.android.flexbox.FlexboxItemDecoration

class FlexItemDecoration(
    context: Context,
    val verticalPadding: Int = context.getDimensionKtx(R.dimen.margin_4).toInt(),
    val horizontalPadding: Int = context.getDimensionKtx(R.dimen.margin_small).toInt(),
) : FlexboxItemDecoration(context) {

    init {
        setDrawable(context.getDrawableKtx(R.drawable.bg_transporent))
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)
    }
}
