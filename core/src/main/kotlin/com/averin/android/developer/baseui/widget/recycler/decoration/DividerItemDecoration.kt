package com.averin.android.developer.baseui.widget.recycler.decoration

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import androidx.recyclerview.widget.RecyclerView
import com.averin.android.developer.baseui.extension.androidx.recyclerview.widget.getItemByChild
import kotlin.math.roundToInt

/**
 * Рисуем разграничители до и(или) после каждого элемента списка.
 * @param dividerBeforeDrawable - drawable, который рисуется до элемента
 * @param dividerAfterDrawable - drawable, который рисуется после элемента
 * @param startPadding - отступ для drawable относительно левого (либо верхнего) края view
 * @param endPadding - отступ для drawable относительно правого (либо нижнего) края view
 * @param orientation - ориентация списка
 * @param dividerBeforePredicate - предикат, определяющий, нужно ли рисовать drawable до элемента
 * @param dividerAfterPredicate - предикат, определяющий, нужно ли рисовать drawable после элемента
 */
@Suppress("LongParameterList")
open class DividerItemDecoration(
    protected val dividerBeforeDrawable: Drawable? = null,
    protected val dividerAfterDrawable: Drawable? = null,
    protected val startPadding: Int = 0,
    protected val endPadding: Int = 0,
    @RecyclerView.Orientation protected val orientation: Int = RecyclerView.VERTICAL,
    protected val dividerBeforePredicate: (item: Any?) -> Boolean = { false },
    protected val dividerAfterPredicate: (item: Any?) -> Boolean = { true }
) : RecyclerView.ItemDecoration() {

    private val viewBounds = Rect()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (dividerAfterDrawable != null || dividerBeforeDrawable != null) {
            val item = parent.getItemByChild(view)
            when (orientation) {
                RecyclerView.HORIZONTAL -> addHorizontalSpacing(outRect, item)
                RecyclerView.VERTICAL -> addVerticalSpacing(outRect, item)
            }
        }
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager != null && (dividerBeforeDrawable != null || dividerAfterDrawable != null)) {
            when (orientation) {
                RecyclerView.HORIZONTAL -> drawHorizontal(canvas, parent)
                RecyclerView.VERTICAL -> drawVertical(canvas, parent)
            }
        }
    }

    private fun addHorizontalSpacing(outRect: Rect, item: Any?) {
        val left = if (dividerBeforeDrawable != null && dividerBeforePredicate(item)) {
            dividerBeforeDrawable.intrinsicWidth
        } else {
            0
        }
        val right = if (dividerAfterDrawable != null && dividerAfterPredicate(item)) {
            dividerAfterDrawable.intrinsicWidth
        } else {
            0
        }
        outRect.set(left, 0, right, 0)
    }

    private fun addVerticalSpacing(outRect: Rect, item: Any?) {
        val top = if (dividerBeforeDrawable != null && dividerBeforePredicate(item)) {
            dividerBeforeDrawable.intrinsicHeight
        } else {
            0
        }
        val bottom = if (dividerAfterDrawable != null && dividerAfterPredicate(item)) {
            dividerAfterDrawable.intrinsicHeight
        } else {
            0
        }
        outRect.set(0, top, 0, bottom)
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        parent.forEach { child ->
            val top = parent.paddingTop + startPadding
            val bottom = parent.height - parent.paddingBottom - endPadding
            parent.getDecoratedBoundsWithMargins(child, viewBounds)
            val item = parent.getItemByChild(child)
            if (dividerBeforeDrawable != null && dividerBeforePredicate(item)) {
                val left = viewBounds.left + child.translationX.roundToInt()
                val right = left + dividerBeforeDrawable.intrinsicWidth
                dividerBeforeDrawable.setBounds(left, top, right, bottom)
                dividerBeforeDrawable.draw(canvas)
            }
            if (dividerAfterDrawable != null && dividerAfterPredicate(item)) {
                val right = viewBounds.right + child.translationX.roundToInt()
                val left = right - dividerAfterDrawable.intrinsicHeight
                dividerAfterDrawable.setBounds(left, top, right, bottom)
                dividerAfterDrawable.draw(canvas)
            }
        }
        canvas.restore()
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        parent.forEachIndexed { index, child ->
            val left = parent.paddingStart + startPadding
            val right = parent.width - parent.paddingEnd - endPadding
            parent.getDecoratedBoundsWithMargins(child, viewBounds)
            val item = parent.getItemByChild(child)
            if (dividerBeforeDrawable != null && dividerBeforePredicate(item)) {
                val top = viewBounds.top + child.translationY.roundToInt()
                val bottom = top + dividerBeforeDrawable.intrinsicHeight
                dividerBeforeDrawable.setBounds(left, top, right, bottom)
                dividerBeforeDrawable.draw(canvas)
            }
            if (dividerAfterDrawable != null && dividerAfterPredicate(item)) {
                val bottom = viewBounds.bottom + child.translationY.roundToInt()
                val top = bottom - dividerAfterDrawable.intrinsicHeight
                dividerAfterDrawable.setBounds(left, top, right, bottom)
                dividerAfterDrawable.draw(canvas)
            }
        }
        canvas.restore()
    }
}
