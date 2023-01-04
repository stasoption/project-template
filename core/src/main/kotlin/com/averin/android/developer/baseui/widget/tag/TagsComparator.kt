package com.averin.android.developer.baseui.widget.tag

import com.averin.android.developer.baseui.presentation.adapter.AdapterDivider
import com.averin.android.developer.baseui.presentation.adapter.AdapterModel

class TagsComparator : Comparator<AdapterModel> {
    override fun compare(o1: AdapterModel, o2: AdapterModel): Int {
        val k1 = getAdapterModelWeight(o1)
        val k2 = getAdapterModelWeight(o2)
        return k1.compareTo(k2)
    }
    private fun getAdapterModelWeight(adapterModel: AdapterModel): Int {
        return when (adapterModel) {
            is Tag -> {
                if (adapterModel.isSelected) {
                    0
                } else {
                    2
                }
            }
            is AdapterDivider -> 1
            else -> 3
        }
    }
}
