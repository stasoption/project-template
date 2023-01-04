package com.averin.android.developer.baseui.presentation.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class RecyclerDragItemTouchHelper(
    private val recyclerView: RecyclerView,
    private val adapterDragListener: AdapterDragListener
) : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {

    private var itemTouchHelper: ItemTouchHelper

    init {
        itemTouchHelper = ItemTouchHelper(this)
    }

    fun attachDrag() {
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    fun detachDrag() {
        itemTouchHelper.attachToRecyclerView(null)
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return if (viewHolder is BaseHeaderViewHolder) {
            0
        } else {
            makeMovementFlags(dragFlags, 0)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        adapterDragListener.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        adapterDragListener.onItemMoved()
        super.clearView(recyclerView, viewHolder)
    }

    override fun isLongPressDragEnabled(): Boolean = true

    override fun isItemViewSwipeEnabled(): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) { }
}
