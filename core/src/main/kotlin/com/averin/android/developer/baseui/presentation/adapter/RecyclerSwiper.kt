package com.averin.android.developer.baseui.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.core.view.get
import androidx.core.view.isNotEmpty
import androidx.core.view.size
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.averin.android.developer.base.NO_VALUE_INT
import java.util.LinkedList
import java.util.Queue
import kotlin.collections.HashMap
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.isNotEmpty
import kotlin.collections.mutableListOf
import kotlin.collections.orEmpty
import kotlin.collections.set
import kotlin.collections.toMutableList

abstract class RecyclerSwiper(
    private val context: Context,
    private val recyclerView: RecyclerView,
    private val isWithHeader: Boolean = false
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private lateinit var buttons: MutableList<UnderlayButton>
    private lateinit var gestureDetector: GestureDetector
    private lateinit var recoverQueue: Queue<Int>

    private var swipedPos = NO_VALUE_INT
    private var swipeThreshold = 0.5f
    private val buttonsBuffer: MutableMap<Int, MutableList<UnderlayButton>>

    private var itemTouchHelper: ItemTouchHelper

    /*
    * button width is same as button height
    * */
    private val buttonWidth: Int get() = when {
        isWithHeader && recyclerView.size > 1 -> {
            recyclerView[1].height
        }
        !isWithHeader && recyclerView.isNotEmpty() -> {
            recyclerView[0].height
        }
        else -> DEFAULT_BUTTON_WIDTH
    }

    private val paddingIcon: Float get() = (buttonWidth / 3).toFloat()

    private val gestureListener: SimpleOnGestureListener = object : SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            for (button in buttons) {
                if (button.onClick(e.x, e.y)) break
            }
            return true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = OnTouchListener { view, e ->
        if (swipedPos < 0) return@OnTouchListener false
        val point = Point(
            e.rawX.toInt(), e.rawY.toInt()
        )
        val swipedViewHolder = recyclerView.findViewHolderForAdapterPosition(swipedPos)
        val swipedItem = swipedViewHolder?.itemView
        val rect = Rect()
        swipedItem?.getGlobalVisibleRect(rect)
        if (e.action == MotionEvent.ACTION_DOWN || e.action == MotionEvent.ACTION_UP || e.action == MotionEvent.ACTION_MOVE) {
            if (rect.top < point.y && rect.bottom > point.y) gestureDetector.onTouchEvent(e) else {
                recoverQueue.add(swipedPos)
                swipedPos = NO_VALUE_INT
                recoverSwipedItem()
            }
        }
        false
    }

    init {
        buttons = mutableListOf()
        gestureDetector = GestureDetector(context, gestureListener)
        recyclerView.setOnTouchListener(onTouchListener)
        buttonsBuffer = HashMap()
        recoverQueue = object : LinkedList<Int>() {
            override fun add(element: Int): Boolean {
                return if (contains(element)) {
                    false
                } else {
                    super.add(element)
                }
            }
        }
        itemTouchHelper = ItemTouchHelper(this)
    }

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        if (isWithHeader && viewHolder is BaseHeaderViewHolder) return 0
        return super.getSwipeDirs(recyclerView, viewHolder)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val pos = viewHolder.adapterPosition
        if (swipedPos != pos) {
            recoverQueue.add(swipedPos)
        }
        swipedPos = pos
        if (buttonsBuffer.containsKey(swipedPos)) {
            buttons = buttonsBuffer[swipedPos].orEmpty().toMutableList()
        } else {
            buttons.clear()
        }
        buttonsBuffer.clear()
        swipeThreshold = 0.5f * buttons.size * buttonWidth
        recoverSwipedItem()
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return swipeThreshold
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return 0.1f * defaultValue
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 5.0f * defaultValue
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val pos = viewHolder.adapterPosition
        var translationX = dX
        val itemView = viewHolder.itemView
        if (pos < 0) {
            swipedPos = pos
            return
        }
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                var buffer: MutableList<UnderlayButton> = mutableListOf()
                if (!buttonsBuffer.containsKey(pos)) {
                    instantiateUnderlayButton(viewHolder, buffer)
                    buttonsBuffer[pos] = buffer
                } else {
                    buffer = buttonsBuffer[pos].orEmpty().toMutableList()
                }
                translationX = dX * buffer.size * buttonWidth / itemView.width
                drawButtons(c, itemView, buffer, pos, translationX)
            }
        }
        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            translationX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    @Synchronized
    private fun recoverSwipedItem() {
        while (recoverQueue.isNotEmpty()) {
            recoverQueue.poll()?.let { pos ->
                if (pos > NO_VALUE_INT) {
                    recyclerView.adapter?.notifyItemChanged(pos)
                }
            }
        }
    }

    private fun drawButtons(
        c: Canvas,
        itemView: View,
        buffer: List<UnderlayButton>,
        pos: Int,
        dX: Float
    ) {
        var right = itemView.right.toFloat()
        val dButtonWidth = NO_VALUE_INT * dX / buffer.size
        for (button in buffer) {
            val left = right - dButtonWidth
            val rect = RectF(
                left,
                itemView.top.toFloat(),
                right,
                itemView.bottom.toFloat()
            )
            button.onDraw(c, rect, pos)
            right = left
        }
    }

    fun attachSwipe() {
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    fun detachSwipe() {
        itemTouchHelper.attachToRecyclerView(null)
    }

    abstract fun instantiateUnderlayButton(
        viewHolder: RecyclerView.ViewHolder,
        underlayButtons: MutableList<UnderlayButton>
    )

    inner class UnderlayButton(
        private val icon: Drawable,
        private val color: Int,
        var underlayButtonClickListener: ((Int) -> Unit)
    ) {
        private var position: Int = 0
        private lateinit var clickRegion: RectF

        fun onClick(x: Float, y: Float): Boolean {
            if (clickRegion.contains(x, y)) {
                underlayButtonClickListener.invoke(position)
                return true
            }
            return false
        }

        fun onDraw(c: Canvas, rect: RectF, pos: Int) {

            clickRegion = rect
            position = pos

            // Draw background
            val p = Paint()
            p.color = color
            c.drawRect(rect, p)

            // Draw Icon
            icon.setBounds(
                (rect.left + paddingIcon).toInt(),
                (rect.top + paddingIcon).toInt(),
                (rect.right - paddingIcon).toInt(),
                (rect.bottom - paddingIcon).toInt()
            )
            icon.draw(c)
        }
    }

    companion object {
        const val DEFAULT_BUTTON_WIDTH = 100
    }
}
