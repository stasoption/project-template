package com.averin.android.developer.baseui.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatSeekBar

class VerticalSeekBar(context: Context, attrs: AttributeSet) : AppCompatSeekBar(context, attrs) {

    var onProgressChangeListener: ((progress: Int) -> Unit)? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(h, w, oldh, oldw)
    }

    @Synchronized
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec)
        setMeasuredDimension(measuredHeight, measuredWidth)
    }

    override fun onDraw(c: Canvas) {
        c.rotate(90f)
        c.translate(0f, -width.toFloat())
        super.onDraw(c)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                progress = (max * event.y / height).toInt()
                onSizeChanged(width, height, 0, 0)

                if (event.action == MotionEvent.ACTION_UP) {
                    performClick()
                    onProgressChangeListener?.invoke(progress)
                }
            }
            MotionEvent.ACTION_CANCEL -> {}
        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}
