package com.statistics.library.line_chart.util

import com.statistics.library.line_chart.VALUE_RESIDUAL
import com.statistics.library.line_chart.data.DataEntity

/**
 * Created by 周智慧 on 28/12/2017.
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

fun getRightValue(value: Int): Int {
    var temp = value
    while (!isRightValue(temp)) {
        temp++
    }
    return temp
}

fun isRightValue(value: Int): Boolean {
    return value % VALUE_RESIDUAL == 0
}

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