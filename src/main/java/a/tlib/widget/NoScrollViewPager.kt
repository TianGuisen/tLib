package a.tlib.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * 可以禁止滑动的viewpager
 */
open class NoScrollViewPager : ViewPager {
    /**
     * 默认禁止滑动，true为可以滑动
     */
    var isScroll = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (isScroll) {
            return super.onInterceptTouchEvent(ev)
        } else {
            return false
        }
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (isScroll) {
            return super.onTouchEvent(ev)
        } else {
            return false
        }
    }
}