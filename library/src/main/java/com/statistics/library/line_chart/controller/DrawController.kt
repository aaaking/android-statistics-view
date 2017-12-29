package com.statistics.library.line_chart.controller

import android.graphics.Canvas
import com.statistics.library.line_chart.LineChartView
import com.statistics.library.line_chart.VERTICAL_PART_VALUE
import com.statistics.library.line_chart.data.AnimEntity

/**
 * Created by 周智慧 on 28/12/2017.
 */
class DrawController(var mView: LineChartView) {
    var animValue: AnimEntity? = null
    fun draw(canvas: Canvas) {
        drawFrameLines(canvas = canvas)
        drawVerticalChart(canvas)
        drawHorizontalChart(canvas)
        drawChart(canvas)
    }

    private fun drawFrameLines(canvas: Canvas) {
        val verticalLineHeight = mView.height - mView.textSize - mView.padding
        //垂直线
        canvas.drawLine(mView.mVerticalTextWidth.toFloat(), mView.heightOffset.toFloat(), mView.mVerticalTextWidth.toFloat(), verticalLineHeight.toFloat(), mView.frameLinePaint)
        //水平线
        canvas.drawLine(mView.mVerticalTextWidth.toFloat(), verticalLineHeight.toFloat(), mView.width.toFloat(), verticalLineHeight.toFloat(), mView.frameLinePaint)
    }

    private fun drawVerticalChart(canvas: Canvas) {
        if (mView.mDatas.isEmpty()) {
            return
        }
        val verticalLineHeight = mView.height - mView.textSize - mView.padding
        val partHeight = (verticalLineHeight - mView.heightOffset) / mView.verticalParts
        var currHeight = verticalLineHeight
        var currTitle = mView.verticalStartValue
        for (i in 0..mView.verticalParts) {
            var titleY = currHeight + mView.textSize / 3
            if (mView.textSize + mView.heightOffset > currHeight) {
                titleY = currHeight + mView.textSize - mView.TEXT_SIZE_OFFSET
            }
            if (i > 0) {
                //绘制间隔线
                canvas.drawLine(mView.mVerticalTextWidth.toFloat(), currHeight.toFloat(), mView.width.toFloat(), currHeight.toFloat(), mView.frameInternalPaint)
            }
            val title = currTitle.toString()
            //绘制垂直文本
            canvas.drawText(title, mView.padding.toFloat(), titleY.toFloat(), mView.frameTextPaint)
            currHeight -= partHeight
            currTitle += VERTICAL_PART_VALUE
        }
    }

    private fun drawHorizontalChart(canvas: Canvas) {
        if (mView.mDatas.isEmpty()) {
            return
        }
        for (i in mView.mDatas.indices) {
            val inputData = mView.mDatas.get(i)
            val date = inputData.des
            val dateWidth = mView.frameTextPaint.measureText(date).toInt()
            var x: Int = inputData.startX
            x -= if (mView.mDatas.size - 1 > i) {
                dateWidth / 2
            } else {
                dateWidth / 2 + 10//最后一个文案向左多偏移一些
                //(dateWidth * 13 / 16f).toInt()//最后一个文案向左多偏移一些
            }
            canvas.drawText(date, x.toFloat(), mView.height.toFloat(), mView.frameTextPaint)
        }
    }

    private fun drawChart(canvas: Canvas) {
        val runningAnimationPosition = animValue?.runningAnimationPosition ?: VALUE_NONE
        for (i in 0 until runningAnimationPosition) {
            drawChart(canvas, i, false)
        }
        if (runningAnimationPosition > VALUE_NONE) {
            drawChart(canvas, runningAnimationPosition, true)
        }
    }

    private fun drawChart(canvas: Canvas, position: Int, isAnimation: Boolean) {
        if (position > mView.mDatas.size - 1) {
            return
        }
        var startX = mView.mDatas[position].startX
        val startY = mView.mDatas[position].startY
        var stopX: Int
        val stopY: Int
        var alpha: Int
        if (isAnimation) {
            stopX = animValue?.x ?: -1
            stopY = animValue?.y ?: -1
            alpha = animValue?.alpha ?: 0
            //这个if条件表示这个动画正好在结束的临界点，在临界点这一时刻的alpha从当前动画的255变成了下一动画的0,手动改过来
            if (stopX >= mView.mDatas[position].stopX && position != mView.mDatas.size - 1) {
                alpha = ALPHA_END//动画在结束的临界点,alpha手动改为最大
            }
        } else {
            stopX = mView.mDatas[position].stopX
            stopY = mView.mDatas[position].stopY
            alpha = ALPHA_END
        }
        drawChart(canvas, startX, startY, stopX, stopY, alpha, position)
    }

    private fun drawChart(canvas: Canvas, startX: Int, startY: Int, stopX: Int, stopY: Int, alpha: Int, position: Int) {
        val radius = mView.radius
        val inerRadius = mView.inerRadius
        mView.strokePaint.setAlpha(alpha)
        canvas.drawLine(startX.toFloat(), startY.toFloat(), stopX.toFloat(), stopY.toFloat(), mView.linePaint)
        canvas.drawCircle(startX.toFloat(), startY.toFloat(), radius.toFloat(), mView.strokePaint)
        canvas.drawCircle(startX.toFloat(), startY.toFloat(), inerRadius.toFloat(), mView.fillPaint)
    }
}