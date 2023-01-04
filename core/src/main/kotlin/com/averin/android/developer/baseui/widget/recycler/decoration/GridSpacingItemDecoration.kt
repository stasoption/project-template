package com.averin.android.developer.baseui.widget.recycler.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.averin.android.developer.baseui.extension.androidx.recyclerview.widget.getItemByChild

/**
 * Добавляет padding между элементами списка в GridLayoutManager
 * @param horizontalSpacing - добавляет padding между элементами одной строки. Половину с конца и половину с начала
 * @param verticalSpacing - добавляет padding между элементами одного столбца. Половину снизу и половину сверху
 * @param verticalEdgeSpacing - добавляет padding к крайним элементам по вертикали
 * @param horizontalEdgeSpacing - добавляет padding к крайним элементам по горизонтали
 * @param spacingPredicate - предикат, определяющий нужно ли добавлять паддинги для данной позиции
 */
open class GridSpacingItemDecoration(
    private val verticalSpacing: Int,
    private val horizontalSpacing: Int,
    private val verticalEdgeSpacing: Int = 0,
    private val horizontalEdgeSpacing: Int = 0,
    protected val spacingPredicate: (item: Any?) -> Boolean = { true }
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager as? GridLayoutManager ?: error("Use with GridLayoutManager only")
        if (spacingPredicate(parent.getItemByChild(view))) {
            var position = parent.getChildAdapterPosition(view)
            if (position == RecyclerView.NO_POSITION) {
                // в процессе анимации удаления adapterPosition == -1, в этом случае используем layoutPosition
                position = parent.getChildLayoutPosition(view)
            }
            if (position != RecyclerView.NO_POSITION) {
                addItemSpacing(parent, layoutManager, position, outRect)
            }
        }
    }

    private fun addItemSpacing(
        parent: RecyclerView,
        layoutManager: GridLayoutManager,
        position: Int,
        outRect: Rect
    ) {
        val spanCount = layoutManager.spanCount
        val spanSizeLookup = layoutManager.spanSizeLookup

        val itemSpanIndex = spanSizeLookup.getSpanIndex(position, spanCount)
        if (itemSpanIndex != 0) {
            outRect.left = horizontalSpacing / 2
        } else {
            outRect.left = horizontalEdgeSpacing
        }
        val nextItemSpanIndex = spanSizeLookup.getSpanIndex(position + 1, spanCount)
        val isLastInRow = nextItemSpanIndex == 0
        if (!isLastInRow) {
            outRect.right = horizontalSpacing / 2
        } else {
            outRect.right = horizontalEdgeSpacing
        }
        val itemRowIndex = spanSizeLookup.getSpanGroupIndex(position, spanCount)
        if (itemRowIndex != 0) {
            outRect.top = verticalSpacing / 2
        } else {
            outRect.top = verticalEdgeSpacing
        }
        val lastItemRowIndex = spanSizeLookup.getSpanGroupIndex(
            parent.adapter?.itemCount ?: position,
            spanCount
        )
        val isInLastRow = lastItemRowIndex == itemRowIndex
        if (!isInLastRow) {
            outRect.bottom = verticalSpacing / 2
        } else {
            outRect.bottom = verticalEdgeSpacing
        }
    }
}
