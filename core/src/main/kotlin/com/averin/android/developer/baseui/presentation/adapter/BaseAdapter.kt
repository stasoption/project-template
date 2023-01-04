package com.averin.android.developer.baseui.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

@SuppressLint("NotifyDataSetChanged")
abstract class BaseAdapter<T : Any, H : BaseViewHolder>(
    private val onItemClickListener: ((T) -> Unit)? = null
) : RecyclerView.Adapter<H>() {

    private lateinit var baseRecyclerView: RecyclerView

    var items: MutableList<T> = mutableListOf()
        set(newOrders) {
            field = newOrders
            notifyDataSetChanged()
        }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        baseRecyclerView = recyclerView
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: H, position: Int) {
        holder.itemView.setOnClickListener { onClickItemView(position) }
        holder.bindData(position)
    }

    open fun onClickItemView(position: Int) {
        onItemClickListener?.invoke(items[position])
    }

    fun ViewGroup.inflateViewHolder(resId: Int): View {
        return LayoutInflater.from(this.context).inflate(resId, this, false)
    }

    fun getItem(position: Int): T? = items.getOrNull(position)
}
