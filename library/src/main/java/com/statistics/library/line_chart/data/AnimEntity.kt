package com.statistics.library.line_chart.data

/**
 * Created by 周智慧 on 28/12/2017.
 */
data class AnimEntity(var x: Int, var y: Int) {
    var alpha: Int = 0//动画执行时的alpha用来绘制圆圈的透明度
    var runningAnimIndex: Int = 0//当前正在执行的动画的index
}