package a.tlib.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

/**
 * @author 田桂森 2020/3/9
 */
open class MsgRecyclerView : RecyclerView {

    var dx = 0.0f
    var dy = 0.0f
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        overScrollMode = View.OVER_SCROLL_NEVER//隐藏边缘
        addOnScrollListener(imageLoadScrollListener)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                dx = ev.getX()
                dy = ev.getY()
            }
            MotionEvent.ACTION_MOVE -> {
                //在上下滑动距离大于左右滑动距离时候请求父控件不要抢事件
                if (Math.abs(ev.getY() - dy)/3*4 < Math.abs(ev.getX() - dx)) {
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            MotionEvent.ACTION_UP -> {
                
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private val imageLoadScrollListener: OnScrollListener = object : OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when (newState) {
                SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING -> {//静止的时候加载图片
                    try {
                        if (context != null) {
                            Glide.with(context).resumeRequests()
                        }
                    } catch (e: Exception) {
                    }
                }
                SCROLL_STATE_SETTLING -> {//在滚动停止加载图片
                    try {
                        if (context != null) {
                            Glide.with(context).pauseRequests()
                        }
                    } catch (e: Exception) {
                    }
                }
            }
        }
    }

}