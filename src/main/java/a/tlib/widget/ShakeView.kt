package a.tlib.widget

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.CycleInterpolator
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * 抖动动画
 */
class ShakeView : androidx.appcompat.widget.AppCompatImageView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    var ratateAnimator: ValueAnimator? = null

    var disposable: Disposable? = null
    private var isDrag = false
    private var lastX = 0
    private var lastY = 0
    private var parentHeight = 0
    private var parentWidth = 0

    private var dx = 0
    private var dy = 0


    private fun init() {
        ratateAnimator = ObjectAnimator.ofFloat(this, "rotation", -7f, 7f)
        ratateAnimator?.interpolator = CycleInterpolator(1f)
        ratateAnimator?.repeatCount = 2
        ratateAnimator?.repeatMode = ObjectAnimator.REVERSE
        ratateAnimator?.duration = 200
    }

    /**
     * 触发动画
     */
    @SuppressLint("CheckResult")
    fun startShakeAnimation() {
        disposable = Observable.interval(1, 3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    clearAnimation()
                    ratateAnimator?.start()
                }
    }

    /**
     * 停止动画
     */
    fun clearShakeAnimation() {
        disposable?.dispose()
        clearAnimation()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val rawX = event!!.rawX.toInt()
        val rawY = event!!.rawY.toInt()
        when (event!!.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                dx = 0
                dy = 0
                isPressed = true
                isDrag = false
                parent.requestDisallowInterceptTouchEvent(true)
                lastX = rawX
                lastY = rawY
                val parent: ViewGroup
                if (getParent() != null) {
                    parent = getParent() as ViewGroup
                    parentHeight = parent.height
                    parentWidth = parent.width
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (parentHeight <= 0 || parentWidth <= 0) {
                    //如果不存在父类的宽高则无法拖动，默认直接返回false
                    isDrag = false
                } else {
                    dx = rawX - lastX
                    dy = rawY - lastY
                    //此处稍微增加一些移动的偏移量，防止手指抖动，误判为移动无法触发点击时间
                    if (dx == 0 && dy == 0) {
                        isDrag = false
                    } else {
                        //程序到达此处一定是正在拖动了
                        isDrag = true
                        var x = x + dx
                        var y = y + dy
                        //检测是否到达边缘 左上右下
                        x = if (x < 0) 0f else if (x > parentWidth - width) (parentWidth - width).toFloat() else x
                        y = if (getY() < 0) 0f else if (getY() + height > parentHeight) (parentHeight - height).toFloat() else y
                        setX(x)
                        setY(y)
                        lastX = rawX
                        lastY = rawY
                    }
                }
            }
        }
        //如果是拖拽则消耗事件，否则正常传递即可。
        return isDrag() || super.onTouchEvent(event)
    }

    fun isDrag(): Boolean {
        return isDrag
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startShakeAnimation()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clearShakeAnimation()
    }
}