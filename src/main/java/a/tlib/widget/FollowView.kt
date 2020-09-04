package a.tlib.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.RelativeLayout

class FollowView : RelativeLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var startX: Float = 0f
    var startY: Float = 0f

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                //获取点击的xy坐标
                startX = event.x
                startY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                this.translationX = x + (event.x - startX)
                this.translationY = y + (event.y - startY)
            }
        }
        return true
    }
}