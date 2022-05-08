package com.statistics.library.line_chart.controller

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Point
import com.statistics.library.line_chart.*
import com.statistics.library.line_chart.data.AnimEntity


/**
 * Created by 周智慧 on 28/12/2017.
 */
class DrawController(var mView: LineChartView) {
    var animValue: AnimEntity? = null
    fun draw(canvas: Canvas) {
        drawFrameLines(canvas = canvas)//绘制x、y坐标轴
        drawVerticalChart(canvas)//绘制垂直文案，
        drawHorizontalChart(canvas)//绘制水平文案，
        drawChart(canvas)//绘制折线和大小圆圈，
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
            currTitle += mView.VERTICAL_PART_VALUE
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
        val runningAnimIndex = animValue?.runningAnimIndex ?: VALUE_NONE
        for (i in 0 until runningAnimIndex) {
            drawChart(canvas, i, false)
        }
        if (runningAnimIndex > VALUE_NONE) {
            drawChart(canvas, runningAnimIndex, true)
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
        val radius = mView.radius.toFloat()
        val inerRadius = mView.inerRadius.toFloat()
        mView.strokePaint.setAlpha(alpha)
        canvas.drawLine(startX.toFloat(), startY.toFloat(), stopX.toFloat(), stopY.toFloat(), mView.linePaint)
        when {
            mView.dotShape == DOT_SHAPE_CIRCLE -> {
                canvas.drawCircle(startX.toFloat(), startY.toFloat(), radius, mView.strokePaint)//外
                canvas.drawCircle(startX.toFloat(), startY.toFloat(), inerRadius, mView.fillPaint)//内
            }
            mView.dotShape == DOT_SHAPE_TRIANGLE -> {
                var longEdge = radius * Math.cos(Math.toRadians(30.0))
                var shortEdge = radius * Math.sin(Math.toRadians(30.0))
                var point1 = Point((startX - longEdge).toInt(), (startY + shortEdge).toInt())
                var point2 = Point((startX - 0).toInt(), (startY - longEdge).toInt())
                var point3 = Point((startX + longEdge).toInt(), (startY + shortEdge).toInt())
                var path = Path()
                path.moveTo(point1.x.toFloat(), point1.y.toFloat())
                path.lineTo(point2.x.toFloat(), point2.y.toFloat())
                path.moveTo(point2.x.toFloat(), point2.y.toFloat())
                path.lineTo(point3.x.toFloat(), point3.y.toFloat())
                path.moveTo(point3.x.toFloat(), point3.y.toFloat())
                path.lineTo(point1.x.toFloat(), point1.y.toFloat())
                path.close()
                canvas.drawPath(path, mView.strokePaint) //外
                var longEdgeInner = inerRadius * Math.cos(Math.toRadians(30.0))
                var shortEdgeInner = inerRadius * Math.sin(Math.toRadians(30.0))
                var point1Inner = Point((startX - longEdgeInner).toInt(), (startY + shortEdgeInner).toInt())
                var point2Inner = Point((startX - 0).toInt(), (startY - longEdgeInner).toInt())
                var point3Inner = Point((startX + longEdgeInner).toInt(), (startY + shortEdgeInner).toInt())
                var path2Inner = Path()
                path2Inner.setFillType(Path.FillType.EVEN_ODD)
                path2Inner.moveTo(point1Inner.x.toFloat(), point1Inner.y.toFloat())
                path2Inner.lineTo(point2Inner.x.toFloat(), point2Inner.y.toFloat())
                path2Inner.lineTo(point3Inner.x.toFloat(), point3Inner.y.toFloat())
                path2Inner.lineTo(point1Inner.x.toFloat(), point1Inner.y.toFloat())
                path2Inner.close()
                canvas.drawPath(path2Inner, mView.fillPaint)//内
            }
            mView.dotShape == DOT_SHAPE_RECTANGLE -> {
                var left = startX - radius
                var top = startY - radius
                var right = startX + radius
                var bottom = startY + radius
                canvas.drawRect(left, top, right, bottom, mView.strokePaint)//外
                left = startX - inerRadius
                top = startY - inerRadius
                right = startX + inerRadius
                bottom = startY + inerRadius
                canvas.drawRect(left, top, right, bottom, mView.fillPaint)//内
            }
        }
    }
}