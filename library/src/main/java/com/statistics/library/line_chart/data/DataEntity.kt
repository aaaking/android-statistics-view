package com.statistics.library.line_chart.data

/**
 * Created by 周智慧 on 28/12/2017.
 */
data class DataEntity(var index: Int) {
    var value: Int = 0//大小
    var millis: Long = 0//
    var des: String = ""//

    var startX: Int = 0//
    var startY: Int = 0//
    var stopX: Int = -1//
    var stopY: Int = -1//
}