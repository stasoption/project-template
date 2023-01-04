package com.averin.android.developer.baseui.extension.androidx.recyclerview.widget

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.averin.android.developer.baseui.presentation.adapter.BaseHeaderViewHolder
import com.hannesdorfmann.adapterdelegates4.dsl.AdapterDelegateViewBindingViewHolder

const val HEADER_FLAG = "Header"

fun RecyclerView.getItemByChild(child: View): Any? {
    return when (val holder = getChildViewHolder(child)) {
        is AdapterDelegateViewBindingViewHolder<*, *> -> holder.item
        is BaseHeaderViewHolder -> HEADER_FLAG
        else -> null
    }
}
