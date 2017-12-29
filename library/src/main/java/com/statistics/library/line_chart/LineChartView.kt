package com.statistics.library.line_chart

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.statistics.library.R
import com.statistics.library.line_chart.controller.AnimController
import com.statistics.library.line_chart.controller.DrawController
import com.statistics.library.line_chart.data.AnimEntity
import com.statistics.library.line_chart.data.DataEntity
import com.statistics.library.line_chart.util.getCoordinateX
import com.statistics.library.line_chart.util.getCoordinateY
import com.statistics.library.line_chart.util.getRightValue
import com.statistics.library.line_chart.util.max
import java.util.concurrent.TimeUnit

/**
 * Created by 周智慧 on 27/12/2017.
 */
val VALUE_RESIDUAL = 5
var DOT_SHAPE_CIRCLE = 0
var DOT_SHAPE_TRIANGLE = 1
var DOT_SHAPE_RECTANGLE = 2
class LineChartView : View {
    var VERTICAL_PART_VALUE = 10//垂直每段的值
    var dotShape = DOT_SHAPE_CIRCLE//0 circle, 1 triangle, 2 rectangle
    var verticalStartValue = -5//垂直的最小值
    var verticalEndValue = 78//垂直的最大值
    var verticalParts = 5//垂直的段数
    val TEXT_SIZE_OFFSET = 10
    var mDatas: ArrayList<DataEntity> = ArrayList<DataEntity>()
    var mVerticalTextWidth = 0
    lateinit var mDrawController: DrawController
    lateinit var mAnimController: AnimController
    var heightOffset: Int = 0
    var padding: Int = 0
    var textSize: Int = 0
    var radius: Int = 0//大圆半径
    var inerRadius: Int = 0//小圆半径
    var line_width: Int = 0//线宽度
    var stroke_width: Int = 0//线宽度
    lateinit var frameTextPaint: Paint
    lateinit var frameLinePaint: Paint
    lateinit var frameInternalPaint: Paint
    lateinit var linePaint: Paint
    var linePaintColor: Int = 0
    lateinit var strokePaint: Paint
    var strokePaintColor: Int = 0
    lateinit var fillPaint: Paint
    var fillPaintColor: Int = 0
    @JvmOverloads constructor(context: Context) : this(context, null)
    @JvmOverloads constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    @JvmOverloads constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        setBackgroundColor(Color.WHITE)
        init(context, attributeSet, defStyleAttr)
    }

    fun init(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) {
        test()
        val res = context.resources
        getStuffFromXml(attributeSet, res)
        mDrawController = DrawController(this)
        mAnimController = AnimController(this)
        heightOffset = (res.getDimension(R.dimen.radius) + res.getDimension(R.dimen.line_width)).toInt()
        padding = res.getDimension(R.dimen.frame_padding).toInt()
        //
        frameTextPaint = Paint()
        frameTextPaint.isAntiAlias = true
        frameTextPaint.textSize = textSize.toFloat()
        frameTextPaint.color = res.getColor(R.color.gray_400)
        //
        frameLinePaint = Paint()
        frameLinePaint.isAntiAlias = true
        frameLinePaint.strokeWidth = res.getDimension(R.dimen.frame_line_width)
        frameLinePaint.color = res.getColor(R.color.gray_400)
        //
        frameInternalPaint = Paint()
        frameInternalPaint.isAntiAlias = true
        frameInternalPaint.strokeWidth = res.getDimension(R.dimen.frame_line_width)
        frameInternalPaint.color = res.getColor(R.color.gray_200)
        //
        linePaint = Paint()
        linePaint.isAntiAlias = true
        linePaint.strokeWidth = line_width.toFloat()
        linePaint.color = linePaintColor
        //
        strokePaint = Paint()
        strokePaint.style = Paint.Style.STROKE
        strokePaint.isAntiAlias = true
        strokePaint.strokeWidth = stroke_width.toFloat()
        strokePaint.color = strokePaintColor
        //
        fillPaint = Paint()
        fillPaint.style = Paint.Style.FILL
        fillPaint.isAntiAlias = true
        fillPaint.color = fillPaintColor
    }
    
    fun test() {
        var data1 = DataEntity(0)
        data1.value = 10
        data1.des = "10s"
        var data2 = DataEntity(1)
        data2.value = 25
        data2.des = "25s"
        var data3 = DataEntity(2)
        data3.value = 20
        data3.des = "20s"
        var data4 = DataEntity(3)
        data4.value = 30
        data4.des = "30s"
        var data5 = DataEntity(4)
        data5.value = 15
        data5.des = "15s"
        var data6 = DataEntity(5)
        data6.value = 68
        data6.des = "68s"
        var data7 = DataEntity(6)
        data7.value = 40
        data7.des = "40s"
        var data8 = DataEntity(7)
        data8.value = 45
        data8.des = "45s"
        mDatas.add(data1)
        mDatas.add(data2)
        mDatas.add(data3)
        mDatas.add(data4)
        mDatas.add(data5)
        mDatas.add(data6)
        mDatas.add(data7)
        mDatas.add(data8)
        var currMillis = System.currentTimeMillis()
        val daysMillis = TimeUnit.DAYS.toMillis(1)
        val temp = currMillis % daysMillis
        currMillis -= temp
        for (i in mDatas.indices) {
            val position = (mDatas.size - 1 - i).toLong()
            val offsetMillis = TimeUnit.DAYS.toMillis(position)
            val millis = currMillis - offsetMillis
            mDatas.get(i).millis = (millis)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val measurewidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val measureheight = View.MeasureSpec.getSize(heightMeasureSpec)
        updateVerticalTextWidth()
        updateDrawData(measurewidth, measureheight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mDrawController.draw(canvas = canvas)
    }

    fun setData(dataList: List<DataEntity>?) {
        if (dataList == null) {
            return
        }
        mDatas.clear()
        mDatas.addAll(dataList)
        updateVerticalTextWidth()
    }

    fun startAnim() {
        post {
            mAnimController.animate()
        }
    }

    fun onAnimationUpdated(value: AnimEntity?) {
        mDrawController.animValue = value
        invalidate()
    }

    private fun updateVerticalTextWidth(): Int {
        if (mDatas.isEmpty()) {
            return 0
        }
        val maxValue = max(mDatas)
        val maxValueStr = maxValue.toString()
        verticalEndValue = getRightValue(maxValue)
        VERTICAL_PART_VALUE = getRightValue((verticalEndValue - verticalStartValue) / verticalParts)
        val titleWidth = frameTextPaint.measureText(maxValueStr).toInt()
        mVerticalTextWidth = padding + titleWidth + padding
        return mVerticalTextWidth
    }

    private fun updateDrawData(width: Int, height: Int) {
        if (mDatas.isEmpty() || width <= 0 || height <= 0) {
            return
        }
        for (i in mDatas.indices) {
            var dataEntity = mDatas[i]
            var leftOffset = heightOffset
            dataEntity.startX = getCoordinateX(mVerticalTextWidth, width, i, mDatas.size, leftOffset)
            val lineHeight = height - padding - textSize - heightOffset
            var value: Float = ((dataEntity.value - verticalStartValue) * lineHeight / (VERTICAL_PART_VALUE * verticalParts)).toFloat()
            dataEntity.startY = getCoordinateY(height - padding - textSize, heightOffset, value)
            //
            var nextPos = i + 1
            if (nextPos < mDatas.size) {
                value = ((mDatas[nextPos].value - verticalStartValue) * lineHeight / (VERTICAL_PART_VALUE * verticalParts)).toFloat()
                dataEntity.stopX = getCoordinateX(mVerticalTextWidth, width, nextPos, mDatas.size, leftOffset)
                dataEntity.stopY = getCoordinateY(height - padding - textSize, heightOffset, value)
            }
        }
    }

    private fun getStuffFromXml(attributeSet: AttributeSet?, res: Resources) {
        var ta = context.obtainStyledAttributes(attributeSet, R.styleable.LineChartView)
        linePaintColor = ContextCompat.getColor(context, ta.getResourceId(R.styleable.LineChartView_lcv_chart_line_color, R.color.login_solid_color))
        strokePaintColor = ContextCompat.getColor(context, ta.getResourceId(R.styleable.LineChartView_lcv_chart_outer_circle_color, R.color.login_solid_color))
        fillPaintColor = ContextCompat.getColor(context, ta.getResourceId(R.styleable.LineChartView_lcv_chart_inner_circle_color, R.color.white))
        radius = ta.getDimensionPixelOffset(R.styleable.LineChartView_lcv_radius, res.getDimensionPixelOffset(R.dimen.radius))
        inerRadius = ta.getDimensionPixelOffset(R.styleable.LineChartView_lcv_inner_radius, res.getDimensionPixelOffset(R.dimen.iner_radius))
        line_width = ta.getDimensionPixelOffset(R.styleable.LineChartView_lcv_line_width, res.getDimensionPixelOffset(R.dimen.line_width))
        stroke_width = ta.getDimensionPixelOffset(R.styleable.LineChartView_lcv_stroke_width, res.getDimensionPixelOffset(R.dimen.stroke_width))
        textSize = ta.getDimensionPixelOffset(R.styleable.LineChartView_lcv_text_size, res.getDimensionPixelOffset(R.dimen.frame_text_size))
        dotShape = ta.getInteger(R.styleable.LineChartView_lcv_dot_shape, DOT_SHAPE_CIRCLE)
    }

    /*
        MiExToast: chartview setData
        MiExToast: chartview onAttachedToWindow
        MiExToast: chartview onMeasure
        MiExToast: chartview onMeasure
        MiExToast: chartview animate
        MiExToast: chartview onDraw
    * */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mAnimController.animatorSet.removeAllListeners()
        mAnimController.animatorSet.cancel()
        mAnimController.animatorSet.end()
        clearAnimation()
        mAnimController.mView = null
    }
}