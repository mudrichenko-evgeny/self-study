package com.mudrichenkoevgeny.selfstudy.ui.custom_view.pie_chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.mudrichenkoevgeny.selfstudy.R

class PieChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var sum: Float = 0f
    var progress: Float = 0f

    init {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.PieChartView).apply {
            try {
                sum = getFloat(R.styleable.PieChartView_sum, 0f)
                progress = getFloat(R.styleable.PieChartView_progress, 0f)
            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawCircle(canvas)
    }

    private fun drawCircle(canvas: Canvas) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
        }
        val size: Float = width.toFloat()

        if (sum <= 0) {
            drawCircleNeutral(canvas, paint, size)
            return
        }

        val progressPercent = if (sum > 0 && sum >= progress) (progress / sum) else 0f
        val angle = getPercentAngle(progressPercent)
        drawCirclePositive(canvas, paint, size, angle)
        drawCircleNegative(canvas, paint, size, angle)
    }

    private fun drawCircleNeutral(canvas: Canvas, paint: Paint, size: Float) {
        paint.color = ContextCompat.getColor(context, R.color.neutral)
        canvas.drawArc(0f, 0f, size, size, 0f, 360f, true, paint)
    }

    private fun drawCirclePositive(canvas: Canvas, paint: Paint, size: Float, percentAngle: Float) {
        paint.color = ContextCompat.getColor(context, R.color.positive)
        canvas.drawArc(0f, 0f, size, size, startAngle, percentAngle, true, paint)
    }

    private fun drawCircleNegative(canvas: Canvas, paint: Paint, size: Float, percentAngle: Float) {
        paint.color = ContextCompat.getColor(context, R.color.negative)
        canvas.drawArc(0f, 0f, size, size, startAngle + percentAngle, 360 - percentAngle, true, paint)
    }

    fun setData(sum: Float, progress: Float) {
        this.sum = sum
        this.progress = progress
        invalidate()
    }

    private fun getPercentAngle(progressPercent: Float): Float {
        return 360 * progressPercent
    }

    companion object {
        const val startAngle = 270f
    }

}