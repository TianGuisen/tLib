package a.tlib.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.FloatRange
import a.tlib.R
import a.tlib.utils.AppUtil
import a.tlib.utils.AutoSizeUtil
import a.tlib.utils.getcolor


/**
 * @author 田桂森 2020/5/29 0029
 */
class LiveSaleView : View {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    val isHorizontalScreen = AppUtil.isHorizontalScreen()
    val mWidth: Float
        get() {
            if (isHorizontalScreen) {
                return AutoSizeUtil.dp2px(190f).toFloat()
            }
            return AutoSizeUtil.dp2px(10f).toFloat()
        }
    val mHeight: Float
        get() {
            if (isHorizontalScreen) {
                return AutoSizeUtil.dp2px(10f).toFloat()
            }
            return AutoSizeUtil.dp2px(190f).toFloat()
        }
    val borderPaint = Paint()
    val bgPaint = Paint()
    val contentPaint = Paint()

    val space = AutoSizeUtil.dp2px(2f).toFloat()//间隙
    val borderRectF: RectF = RectF(0f, 0f, mWidth, mHeight)

    val bgRectF: RectF = RectF(space, space, mWidth - space, mHeight - space)//底色

    val totalSales500 = 3333f
    val totalSales1000 = 2000f
    val totalSales5000 = 8500f
    var progress = 0.0f//进度比例

    init {
        borderPaint.apply {
            isAntiAlias = true//抗锯齿
            color = Color.parseColor("#44ffffff")
            style = Paint.Style.STROKE//描边
            strokeWidth = 2f//描边宽度
//            setShadowLayer()//阴影
        }
        bgPaint.apply {
            isAntiAlias = true//抗锯齿
            color = Color.parseColor("#44D3D3D3")
            style = Paint.Style.FILL
        }
        contentPaint.apply {
            isAntiAlias = true//抗锯齿
            if (isHorizontalScreen) {
                shader = LinearGradient(0f, 0f, mWidth, mHeight, getcolor(R.color.f7b), getcolor(R.color.ff03), Shader.TileMode.MIRROR)
            } else {
                shader = LinearGradient(0f, 0f, mWidth, mHeight, getcolor(R.color.ff03), getcolor(R.color.f7b), Shader.TileMode.MIRROR)
            }
            style = Paint.Style.FILL
        }
    }

    /**
     * 通用设置，wrap_content时候指定大小
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 获取宽-测量规则的模式和大小
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        // 获取高-测量规则的模式和大小
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        // 设置wrap_content的默认宽 / 高值
        // 默认宽/高的设定并无固定依据,根据需要灵活设置
        // 类似TextView,ImageView等针对wrap_content均在onMeasure()对设置默认宽 / 高值有特殊处理,具体读者可以自行查看

        // 当布局参数设置为wrap_content时，设置默认值
        if (layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT && layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(mWidth.toInt(), mHeight.toInt())
            // 宽 / 高任意一个布局参数为= wrap_content时，都设置默认值
        } else if (layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(mWidth.toInt(), heightSize)
        } else if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(widthSize, mHeight.toInt())
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isHorizontalScreen) {
            canvas.drawRoundRect(borderRectF, mHeight / 2, mHeight / 2, borderPaint)
            canvas.drawRoundRect(bgRectF, mHeight / 2, mHeight / 2, bgPaint)
            if (progress > 0.0f) {
                val rectF3 = RectF(space, space, space + (mWidth * progress), mHeight - space)
                canvas.drawRoundRect(rectF3, mHeight / 2, mHeight / 2, contentPaint)
            }
        } else {
            canvas.drawRoundRect(borderRectF, mWidth / 2, mWidth / 2, borderPaint)
            canvas.drawRoundRect(bgRectF, mWidth / 2, mWidth / 2, bgPaint)
            if (progress > 0.0f) {
                val rectF3 = RectF(0f + space, space + mHeight - (mHeight * progress), mWidth - space, mHeight - space)
                canvas.drawRoundRect(rectF3, mWidth / 2, mWidth / 2, contentPaint)
            }
        }
    }


    fun setProgres(@FloatRange(from = 0.0, to = 1.0) progress: Float) {
        this.progress = progress
        invalidate()
    }

    /**
     * 设置销售额
     */
    fun setSale(sale: Float) {
        if (sale <= 0) {
            setProgres(0.0f)
        } else if (sale <= 150) {
            setProgres(0.05f)
        } else if (sale < 500) {
            setProgres(sale / totalSales500)
        } else if (sale <= 1000) {
            setProgres(500 / totalSales500 + (sale - 500) / totalSales1000)
        } else if (sale <= 5000) {
            setProgres(500 / totalSales500 + 500 / totalSales1000 + (sale - 1000) / totalSales5000)
        } else {
            setProgres(0.95f)
        }
    }
}