package a.tlib.widget

import a.tlib.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import a.tlib.logger.YLog


/**
 * @author 田桂森 2021/3/24 0024
 */


/**
 * 实现一个可选择的TextView，作用是：右上角的角标45°文本提示
 *
 * created by OuyangPeng at 2019/1/10 下午 08:19
 *
 * @author OuyangPeng
 */

class RotateTextView : View {
    private var mWidth = 0
    private var mHeight = 0
    var text = ""
    val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RotateTextView)
        text = a.getText(R.styleable.RotateTextView_t_text).toString()
        var textColor = a.getColor(R.styleable.RotateTextView_t_textColor, 0)
        var textSize = a.getDimension(R.styleable.RotateTextView_t_textSize, 0.0f)
        textPaint.color = textColor
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = textSize
        a.recycle()
    }

    protected override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = getMeasuredWidth()
        mHeight = getMeasuredHeight()
        //获取SingleTouchView所在父布局的中心点
        val mViewGroup = parent as ViewGroup
        if (null != mViewGroup) {
            val mParentWidth = mViewGroup.width
            val mParentHeight = mViewGroup.height
            YLog.d("" + mParentWidth + "---" + mParentHeight)
        }
        // 改变控件的宽高
        setMeasuredDimension(getTextRect(textPaint, text).height() + 35, getTextRect(textPaint, text).width() + 35)
    }

    protected override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    fun setText1(str: String) {
        this.text = str
        if (text.length > 17) {
            text = text.substring(0, 17) + "..."
        }
        requestLayout()
    }

    protected override fun onDraw(canvas: Canvas) {
        // 改变Canvas坐标系，使文字在竖直方向绘制
//        canvas.translate(getTextRect(textPaint, text).height() *1f+13, 0f)
        canvas.rotate(90f)
        // 借助StaticLayout使文字能够自动换行
//        val myStaticLayout = StaticLayout(text, textPaint, mWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false)
//        myStaticLayout.draw(canvas)
        canvas.drawText(text, 0f, -18f, textPaint)
    }

    fun getTextRect(paint: Paint, str: String): Rect {
        val rect = Rect()
        paint.getTextBounds(str, 0, str.length - 1, rect)
        return rect
    }
}