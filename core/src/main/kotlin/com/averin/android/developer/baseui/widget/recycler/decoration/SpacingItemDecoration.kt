package com.averin.android.developer.baseui.widget.recycler.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.averin.android.developer.baseui.extension.androidx.recyclerview.widget.getItemByChild
import timber.log.Timber

/**
 * Добавляет пробелы между элементами, а также пробелы от краев.
 * @param betweenItems - суммарное расстояние между элементами.
 * половина будет добавлена до каждого элемента, оставшаяся часть после.
 * @param paddingLeft - отступ перед первым элементом слева, если список горизонтальный,
 * либо перед каждым элементов справа, если список вертикальный.
 * @param paddingTop - отступ над первым элементом сверху, если список вертикальный,
 * либо над каждым элементом сверху, если список горизонтальный.
 * @param paddingRight - отступ после последнего элемента справа, если список горизонтальный,
 * либо после каждого элемента справа, если список вертикальный.
 * @param paddingBottom - отступ под последним элементом снизу, если список вертикальный,
 * либо под каждым элементом снизу, если список вертикальный.
 * @param orientation - ориентация списка, горизонтальный или вертикальный.
 * @param spacingPredicate - предикат, определяющий нужно ли добавлять паддинги для данной позиции
 * @sample SpacingItemDecoration(3,10, 5, 10, 5, RecyclerView.HORIZONTAL) - добавит 10px слева от первого элемента,
 * 10px после последнего элемента, по 5 пикселей над и под каждым элементом
 * и по 3 пикселя между всеми остальными элементами.
 */
@Suppress("LongParameterList")
open class SpacingItemDecoration(
    betweenItems: Int,
    protected val paddingLeft: Int = 0,
    protected val paddingTop: Int = 0,
    protected val paddingRight: Int = 0,
    protected val paddingBottom: Int = 0,
    @RecyclerView.Orientation protected val orientation: Int = RecyclerView.HORIZONTAL,
    protected val spacingPredicate: (item: Any?) -> Boolean = { true }
) : RecyclerView.ItemDecoration() {

    constructor(
        betweenItems: Int,
        padding: Int = 0,
        orientation: Int = RecyclerView.HORIZONTAL
    ) : this(
        betweenItems,
        padding,
        padding,
        padding,
        padding,
        orientation
    )

    /**
     * Часть пробела, которая будет добавлена перед каждым элементом
     */
    private val spacingBeforeItem = betweenItems / 2

    /**
     * Часть пробела, которая будет добавлена после каждого элемента
     */
    private val spacingAfterItem = betweenItems - spacingBeforeItem

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        var position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) {
            // в процессе анимации удаления adapterPosition == -1, в этом случае используем layoutPosition
            position = parent.getChildLayoutPosition(view)
        }
        if (position == RecyclerView.NO_POSITION) {
            Timber.tag(javaClass.simpleName).e("No position for item in RecyclerView")
            return
        }
        if (spacingPredicate(parent.getItemByChild(view))) {
            when (orientation) {
                RecyclerView.HORIZONTAL -> addHorizontalSpacing(position, state, outRect)
                RecyclerView.VERTICAL -> addVerticalSpacing(position, state, outRect)
            }
        }
    }

    /**
     * Добавляем пробелы для горизонтального списка
     */
    private fun addHorizontalSpacing(position: Int, state: RecyclerView.State, outRect: Rect) {
        outRect.left = spacingBeforeItem
        outRect.top = paddingTop
        outRect.right = spacingAfterItem
        outRect.bottom = paddingBottom
        when (position) {
            FIRST_POSITION -> {
                outRect.left = paddingLeft
            }
            state.itemCount - 1 -> {
                outRect.right = paddingRight
            }
        }
    }

    /**
     * Добавляем пробелы для вертикального списка
     */
    private fun addVerticalSpacing(position: Int, state: RecyclerView.State, outRect: Rect) {
        outRect.left = paddingLeft
        outRect.top = spacingBeforeItem
        outRect.right = paddingRight
        outRect.bottom = spacingAfterItem
        when (position) {
            FIRST_POSITION -> {
                outRect.top = paddingTop
            }
            state.itemCount - 1 -> {
                outRect.bottom = paddingBottom
            }
        }
    }

    companion object {
        protected const val FIRST_POSITION = 0
    }
}
