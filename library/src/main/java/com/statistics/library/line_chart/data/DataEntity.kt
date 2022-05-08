package com.statistics.library.line_chart.data

/**
 * Created by 周智慧 on 28/12/2017.
 */
data class DataEntity(var index: Int) {
    var value: Int = 0//大小，代表值，根据这个值计算出纵坐标位置startY
    var millis: Long = 0//
    var des: String = ""//

    var startX: Int = 0//代表横坐标位置
    var startY: Int = 0//代表纵坐标位置
    var stopX: Int = -1//代表下一个点的横坐标位置
    var stopY: Int = -1//代表下一个点的纵坐标位置
}