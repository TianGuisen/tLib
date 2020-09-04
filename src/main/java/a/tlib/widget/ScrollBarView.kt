package a.tlib.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * 滑动banner显示条
 */
class ScrollBarView : View {
    /**
     * 未选中Paint
     */
    var unSelectPaint: Paint? = null

    /**
     * 选中Paint
     */
    var selectPaint: Paint? = null

    /**
     * 高
     */
    var height: Float = 0f

    /**
     * 宽
     */
    var width: Float = 0f

    /**
     * 可供选择的状态的数量
     */
    var count: Int = 1

    /**
     * 未选中原点的半径
     */
    var radius: Float = 10f

    /**
     * 选中的位置
     */
    var selectPosition: Int = 0

    /**
     * 选中点的宽度
     */
    var selectPointWidth = 35f

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    /**
     * 初始化
     */
    private fun init() {
        unSelectPaint = Paint()
        unSelectPaint?.let {
            it.color = Color.parseColor("#C4C4C4")
            it.strokeWidth = 20f
            it.isAntiAlias = true
            it.style = Paint.Style.FILL
        }

        selectPaint = Paint()
        selectPaint?.let {
            it.color = Color.GRAY
            it.strokeWidth = 20f
            it.isAntiAlias = true
            it.style = Paint.Style.FILL
        }
    }

    /**
     * 设置选中的位置
     */
    fun setPosition(position: Int): View {
        selectPosition = position
        invalidate()
        return this
    }

    /**
     * 设置选择项的数量
     */
    fun setTabCount(count: Int): View {
        this.count = count
        invalidate()
        return this
    }

    /**
     * 设置选中Tab的宽度
     */
    fun setSelectTabWidth(width: Float): View {
        this.selectPointWidth = width
        invalidate()
        return this
    }

    /**
     * 设置tab的半径
     */
    fun setTabRadius(radius: Float): View {
        this.radius = radius
        invalidate()
        return this
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        width = MeasureSpec.getSize(widthMeasureSpec).toFloat() / (2 * count)
        height = MeasureSpec.getSize(heightMeasureSpec).toFloat() / 2

    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (count > 1) {
            for (i in 0 until count) {
                var temp = i * 2 + 1
                if (selectPosition == i) {
                    var rectF = RectF(width * temp - selectPointWidth / 2, height - radius, width * temp + selectPointWidth / 2, height + radius)
                    canvas?.drawRoundRect(rectF, radius, radius, selectPaint!!)
                } else {
                    canvas?.drawCircle(width * temp, height, radius, unSelectPaint!!)
                }
            }
        }
    }
}