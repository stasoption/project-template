package com.averin.android.developer.baseui.presentation.adapter

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

abstract class AbstractPaginationAdapter<T : Any> @JvmOverloads constructor(
    loadOffset: Int = LOAD_OFFSET,
    /*
    * if there's no diffCallback provided, we use default callback
    * */
    diffCallback: DiffUtil.ItemCallback<T> = EqualsDiffItemCallback()
) : AsyncListDifferDelegationAdapter<T>(diffCallback) {

    var callback: Callback? = null
    protected var loadOffset = LOAD_OFFSET
    protected var paginationEnabled = true

    init {
        this.loadOffset = loadOffset
    }

    constructor(callback: Callback) : this(
        LOAD_OFFSET
    ) {
        this.callback = callback
    }

    constructor(loadOffset: Int, callback: Callback) : this(loadOffset) {
        this.callback = callback
    }

    @CallSuper
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (paginationEnabled && position == itemCount - loadOffset) {
            callback?.loadMore()
        }

        super.onBindViewHolder(holder, position)
    }

    @CallSuper
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<*>) {
        if (paginationEnabled && position == itemCount - loadOffset) {
            callback?.loadMore()
        }

        super.onBindViewHolder(holder, position, payloads)
    }

    interface Callback {
        fun loadMore()
    }

    companion object {
        const val LOAD_OFFSET = 4
    }
}
