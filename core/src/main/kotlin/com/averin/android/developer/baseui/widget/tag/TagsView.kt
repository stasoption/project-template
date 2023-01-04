package com.averin.android.developer.baseui.widget.tag

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.baseui.extension.android.content.getColorKtx
import com.averin.android.developer.baseui.presentation.adapter.AdapterDivider
import com.averin.android.developer.baseui.presentation.adapter.AdapterModel
import com.averin.android.developer.baseui.presentation.adapter.BaseAdapter
import com.averin.android.developer.baseui.presentation.adapter.BaseViewHolder
import com.averin.android.developer.core.R
import com.averin.android.developer.core.databinding.LiTagDividerBinding
import com.averin.android.developer.core.databinding.LiTagValueBinding
import com.averin.android.developer.core.databinding.WTagsBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

@SuppressLint("NotifyDataSetChanged")
class TagsView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val binding by viewBinding(WTagsBinding::bind)
    private val tagsView: RecyclerView by lazy { binding.rvTags }
    private val tagsAdapter = TagsAdapter()

    var listForSearching = listOf<AdapterModel>()
    var onSelectedTagsListener: ((selectedTags: List<Tag>) -> Unit)? = null
    var dividerTextRes: Int = R.string.select_from_list

    var tags: List<Tag> = listOf()
        set(value) {
            field = value
            val adapterModelList = mutableListOf<AdapterModel>().apply {
                add(AdapterDivider(context.getString(dividerTextRes)))
                addAll(field)
            }
            adapterModelList.sortWith(TagsComparator())
            tagsAdapter.items = adapterModelList
            listForSearching = adapterModelList
            onSelectedTagsListener?.invoke(field.filter { it.isSelected })
        }
        get() {
            return tagsAdapter.items
                .filterIsInstance<Tag>()
        }

    private val selectedTags: List<Tag>
        get() = tags.filter { it.isSelected }

    init {
        View.inflate(context, R.layout.w_tags, this)
        tagsView.apply {
            layoutManager = FlexboxLayoutManager(context).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
                justifyContent = JustifyContent.FLEX_START
            }
            adapter = tagsAdapter
        }
    }

    fun onClick(model: AdapterModel) {
        if (model is Tag) {
            model.isSelected = !model.isSelected
            tagsAdapter.items.sortWith(TagsComparator())
            tagsAdapter.notifyDataSetChanged()
            onSelectedTagsListener?.invoke(selectedTags)
        }
    }

    fun addTag(tag: Tag) {
        tagsAdapter.items.add(tag)
        tagsAdapter.items.sortWith(TagsComparator())
        listForSearching = tagsAdapter.items
        tagsAdapter.notifyDataSetChanged()
        onSelectedTagsListener?.invoke(selectedTags)
    }

    fun unsekectAll() {
        tagsAdapter.apply {
            items.forEach { item ->
                if (item is Tag) { item.isSelected = false }
            }
            items.sortWith(TagsComparator())
            notifyDataSetChanged()
        }
        onSelectedTagsListener?.invoke(selectedTags)
    }

    private fun AdapterModel.filterTag(searchRequest: String): Boolean {
        if (searchRequest.isEmpty()) {
            return true
        }
        return when {
            this is Tag && this.isSelected -> true
            this is AdapterDivider -> true
            this is Tag && !this.isSelected -> {
                this.title?.lowercase()?.contains(searchRequest.lowercase()) == true
            }
            else -> true
        }
    }

    protected inner class TagsAdapter : BaseAdapter<AdapterModel, BaseViewHolder>({ onClick(it) }) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            val layoutId = if (viewType == ITEM_TAG) {
                R.layout.li_tag_value
            } else {
                R.layout.li_tag_divider
            }
            val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
            return if (viewType == ITEM_TAG) {
                TagsViewHolder(view)
            } else {
                DividerViewHolder(view)
            }
        }

        override fun getItemViewType(position: Int): Int {
            return when (val item = items[position]) {
                is Tag -> ITEM_TAG
                is AdapterDivider -> ITEM_DIVIDER
                else -> throw IllegalArgumentException("$item")
            }
        }

        private inner class DividerViewHolder(itemView: View) : BaseViewHolder(itemView) {
            private val binding by viewBinding(LiTagDividerBinding::bind)
            override fun bindData(position: Int) {
                val divider = items[position] as AdapterDivider
                binding.run {
                    tvDividerTitle.text = divider.title
                    vTagsDivider.isVisible = selectedTags.isNotEmpty()
                }
            }
        }

        private inner class TagsViewHolder(itemView: View) : BaseViewHolder(itemView) {
            private val binding by viewBinding(LiTagValueBinding::bind)
            override fun bindData(position: Int) {
                val tag = items[position] as Tag
                binding.run {
                    tvCandidateTag.text = tag.title
                    ivRemove.isVisible = tag.isSelected

                    val colorPair = tag.tagColorsResourcePair()
                    val bgColorId = colorPair.first
                    val txtColorId = colorPair.second
                    ltRoot.background.apply {
                        colorFilter = PorterDuffColorFilter(
                            context.getColorKtx(bgColorId),
                            PorterDuff.Mode.SRC_IN
                        )
                    }
                    ivRemove.colorFilter = PorterDuffColorFilter(
                        context.getColorKtx(txtColorId),
                        PorterDuff.Mode.SRC_IN
                    )
                    tvCandidateTag.setTextColor(context.getColorKtx(txtColorId))
                }
            }
        }
    }

    companion object {
        private const val ITEM_DIVIDER = 1
        private const val ITEM_TAG = 2
    }
}
