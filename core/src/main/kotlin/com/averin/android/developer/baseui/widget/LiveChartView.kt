package com.averin.android.developer.baseui.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.base.extension.kotlin.makeCapitalize
import com.averin.android.developer.baseui.extension.android.content.getColorKtx
import com.averin.android.developer.baseui.extension.android.content.getDimensionKtx
import com.averin.android.developer.baseui.extension.android.view.getColor
import com.averin.android.developer.core.R
import com.averin.android.developer.core.databinding.WLiveChartBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet

class LiveChartView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val binding by viewBinding(WLiveChartBinding::bind)
    private val barChart: BarChart by lazy { binding.horizontalBarChart }

    private var textColorX: Int = Color.BLACK
    private var textColorY: Int = Color.BLACK

    private val chartColors = listOf(
        Color.parseColor("#CC9D00"),
        Color.parseColor("#AB73F4"),
        Color.parseColor("#4690FF"),
        Color.parseColor("#5BC16F"),
        Color.parseColor("#4EB8EE"),
        Color.parseColor("#FF9339"),
        Color.parseColor("#FB5C76")
    )

    init {
        View.inflate(context, R.layout.w_live_chart, this)
        val array = context.theme.obtainStyledAttributes(attrs, R.styleable.CandidateEmotionsChart, 0, 0)
        try {
            textColorX = array.getColor(R.styleable.CandidateEmotionsChart_chartTextColorX, getColor(R.color.typography_title))
            textColorY = array.getColor(R.styleable.CandidateEmotionsChart_chartTextColorY, getColor(R.color.typography_placeholder))
        } finally {
            array.recycle()
        }
        createChart()
    }

    private fun createChart() {
        barChart.xAxis.run {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawAxisLine(true)
            setDrawGridLines(false)
            granularity = 1f
            textColor = textColorX
        }

        barChart.axisLeft.run {
            axisLineColor = Color.TRANSPARENT
            setDrawGridLines(true)
            setDrawLabels(false)
            axisMinimum = 0f
            axisMaximum = 100f
            gridColor = context.getColorKtx(R.color.transparent_highlight)
            gridLineWidth = context.getDimensionKtx(R.dimen.grid_line_width)
            setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        }

        barChart.axisRight.run {
            setDrawAxisLine(false)
            setDrawGridLines(false)
            textColor = textColorY
            axisMinimum = 0f
            axisMaximum = 100f
        }

        barChart.description.isEnabled = false
//        barChart.axisRight.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.setPinchZoom(false)
        barChart.isDragEnabled = false
        barChart.isDoubleTapToZoomEnabled = false
        barChart.isHighlightPerTapEnabled = true
        barChart.isHighlightPerDragEnabled = true
    }

    fun updateChart(chartFeed: List<ChartFeed>) {
        barChart.clear()
        val bars: ArrayList<BarEntry> = ArrayList()
        for (i in chartFeed.indices) {
            val chartBarData = chartFeed[i]
            bars.add(BarEntry(i.toFloat(), chartBarData.value, chartBarData.title))
        }
        val set = BarDataSet(bars, "emotions").apply {
            colors = chartColors
            highLightColor = Color.TRANSPARENT
            isHighlightEnabled = true
            setDrawValues(true)
        }
        val dataSets = ArrayList<IBarDataSet>().apply {
            add(set)
        }

        barChart.xAxis.valueFormatter = XAxisFormatter(chartFeed)
        barChart.data = BarData(dataSets).apply {
            barWidth = 0.35f
            setValueTextColor(textColorX)
            setValueTextSize(14f)
            setValueFormatter(BarDataValueFormatter())
        }
        barChart.setFitBars(true)
        barChart.invalidate()
    }

    class BarDataValueFormatter() : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return value.toInt().toString()
        }
    }

    class XAxisFormatter(val feed: List<ChartFeed>) : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase): String {
            return feed[value.toInt()].title.makeCapitalize()
        }
    }

    class ChartFeed(
        val title: String,
        val value: Float
    )
}
