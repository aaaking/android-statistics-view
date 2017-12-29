package com.statistics.library.line_chart.controller

import android.animation.AnimatorSet
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import com.statistics.library.line_chart.LineChartView
import com.statistics.library.line_chart.data.AnimEntity
import com.statistics.library.line_chart.data.DataEntity
import java.lang.ref.WeakReference

/**
 * Created by 周智慧 on 28/12/2017.
 */
val PROPERTY_X = "PROPERTY_X"
val PROPERTY_Y = "PROPERTY_Y"
val PROPERTY_ALPHA = "PROPERTY_ALPHA"

val VALUE_NONE = -1
val ALPHA_START = 0
val ALPHA_END = 255
private val ANIMATION_DURATION = 300
class AnimController(view: LineChartView) {
    var mView: WeakReference<LineChartView>? = WeakReference<LineChartView>(view)
    var animatorSet: AnimatorSet = AnimatorSet()

    fun animate() {
        this.animatorSet.playSequentially(mView?.get()?.mDatas?.map { createAnimator(it) })
        animatorSet.start()
    }

    /**
     * 包含三个子动画：alpha动画、x动画、y动画
     */
    private fun createAnimator(drawData: DataEntity): ValueAnimator? {
        var duration = ANIMATION_DURATION.toLong()
        if (drawData.stopX <= -1) {//表示是最后一个点，那么x动画、y动画都指向自己
            drawData.stopX = drawData.startX
        }
        if (drawData.stopY <= -1) {//表示是最后一个点，那么x动画、y动画都指向自己
            drawData.stopY = drawData.startY
        }
        val propertyX = PropertyValuesHolder.ofInt(PROPERTY_X, drawData.startX, drawData.stopX)
        val propertyY = PropertyValuesHolder.ofInt(PROPERTY_Y, drawData.startY, drawData.stopY)
        val propertyAlpha = PropertyValuesHolder.ofInt(PROPERTY_ALPHA, ALPHA_START, ALPHA_END)
        val animator = ValueAnimator()
        animator.setValues(propertyX, propertyY, propertyAlpha)
        animator.duration = duration
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener { valueAnimator -> this@AnimController.onAnimationUpdate(valueAnimator) }
        return animator
    }

    private fun onAnimationUpdate(valueAnimator: ValueAnimator?) {
        if (valueAnimator == null) {
            return
        }
        val value = AnimEntity(valueAnimator.getAnimatedValue(PROPERTY_X) as Int, valueAnimator.getAnimatedValue(PROPERTY_Y) as Int)
        value.alpha = valueAnimator.getAnimatedValue(PROPERTY_ALPHA) as Int
        value.runningAnimIndex = getRunningAnimIndex()
        mView?.get()?.onAnimationUpdated(value)//使用弱引用，否则这里可能内存泄漏
//        if (value.runningAnimIndex <= 1) {
//            Log.i(TAG, "value.runningAnimIndex: ${value.runningAnimIndex}   value.alpha: ${value.alpha}  value.x: ${value.x}")
//            /*
//            * MiExToast: value.runningAnimIndex: 0   value.alpha: 254  value.x: 259
//              MiExToast: value.runningAnimIndex: 0   value.alpha: 255  value.x: 260
//              MiExToast: value.runningAnimIndex: 0   value.alpha: 0  value.x: 260
//              MiExToast: value.runningAnimIndex: 1   value.alpha: 0  value.x: 260
//              MiExToast: value.runningAnimIndex: 1   value.alpha: 0  value.x: 260
//            * */
//        }
    }

    /**
     * 获取当前正在执行的动画的index，这里有个坑，这个动画临界结束的时候，这个动画的属性值会被下个即将开始的动画的属性值代替掉
     */
    private fun getRunningAnimIndex(): Int {
        val childAnimations = animatorSet.childAnimations
        for (i in childAnimations.indices) {
            val animator = childAnimations[i]
            if (animator.isRunning) {
                return i
            }
        }
        return VALUE_NONE
    }
}