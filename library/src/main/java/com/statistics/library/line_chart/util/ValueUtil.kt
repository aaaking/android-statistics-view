package com.statistics.library.line_chart.util

import com.statistics.library.line_chart.VALUE_RESIDUAL
import com.statistics.library.line_chart.data.DataEntity

/**
 * Created by 周智慧 on 28/12/2017.
 */

/**
 * 找出点列表最大的值，根据最大值决定纵向文案的宽度
 */
fun max(dataList: List<DataEntity>?): Int {
    var maxValue = 0
    if (dataList == null || dataList.isEmpty()) {
        return maxValue
    }
    dataList
            .asSequence()
            .filter { it.value > maxValue }
            .forEach { maxValue = it.value }
    return maxValue
}

/**
 * 计算出纵坐标最大值、纵坐标每段的值，两者都是VALUE_RESIDUAL(默认5)的倍数
 */
fun getRightValue(value: Int): Int {
    var temp = value
    while (!isRightValue(temp)) {
        temp++
    }
    return temp
}

/**
 * 是VALUE_RESIDUAL(默认5)的倍数
 */
fun isRightValue(value: Int): Boolean {
    return value % VALUE_RESIDUAL == 0
}

/**
 * 计算点的X坐标
 */
fun getCoordinateX(offset: Int, width: Int, index: Int, numOfPoint: Int, leftOffset: Int): Int {
    val widthCorrected = width - offset
    val partWidth = widthCorrected / (numOfPoint - 1)
    var coordinate = offset + partWidth * index
    if (coordinate < 0) {
        coordinate = 0
    } else if (coordinate > width) {
        coordinate = width
    }
    if (index > 0) {
        coordinate -= leftOffset//圆圈向左偏移
    }
    return coordinate
}

/**
 * 计算点的Y坐标
 */
fun getCoordinateY(height: Int, heightOffset: Int, value: Float): Int {
    val heightCorrected = height - heightOffset
    var coordinate = (heightCorrected - value).toInt()

    if (coordinate < 0) {
        coordinate = 0

    } else if (coordinate > heightCorrected) {
        coordinate = heightCorrected
    }

    coordinate += heightOffset
    return coordinate
}