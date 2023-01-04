package com.averin.android.developer.baseui.presentation.adapter

interface AdapterDragListener {
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onItemMoved()
}
