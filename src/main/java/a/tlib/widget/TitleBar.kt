package a.tlib.widget

import a.tlib.LibApp
import a.tlib.R
import a.tlib.utils.getcolor
import a.tlib.utils.isNotNullEmpty
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.ruffian.library.widget.RFrameLayout
import com.ruffian.library.widget.RTextView
import com.ruffian.library.widget.RView

open class TitleBar : RFrameLayout {

    companion object {
        /**
         * 白字和图标
         */
        const val WHITE_STYLE = 0

        /**
         * 黑字和图标
         */
        const val BLACK_STYLE = 1

        /**
         * 默认的风格
         */
        @JvmStatic
        var defaultStype = WHITE_STYLE
    }

    lateinit var view: TitleBar
    val tv_title by lazy { view.findViewById<TextView>(R.id.tv_title) }
    val fl_back by lazy { view.findViewById<FrameLayout>(R.id.fl_back) }
    val iv_back by lazy { view.findViewById<ImageView>(R.id.iv_back) }
    val iv_right by lazy { view.findViewById<ImageView>(R.id.iv_right) }
    val fl_right by lazy { view.findViewById<FrameLayout>(R.id.fl_right) }
    val tv_right by lazy { view.findViewById<RTextView>(R.id.tv_right) }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    open fun init() {
        view = RView.inflate(context, R.layout.view_title, this) as TitleBar
    }

    fun setTitle(string: String?, style: Int = TitleBar.defaultStype): TextView {
        if (string.isNotNullEmpty()) {
            tv_title.text = string
        }
        when (style) {
            BLACK_STYLE -> {
                setBlackStyle()
            }
            WHITE_STYLE -> {
                setWhiteStyle()
            }
        }
        return tv_title
    }

    fun setTitleColor(color: Int) {
        tv_title.setTextColor(getcolor(color))
    }

    fun setBarColor(color: Int) {
        view.helper.setBackgroundColorNormal(getcolor(color))
    }

    /**
     *  黑字
     */
    fun setBlackStyle() {
        tv_title.setTextColor(getcolor(R.color.c_111))
        iv_back.setImageResource(R.drawable.img_titlebar_back)
        tv_right.setTextColor(getcolor(R.color.c_111))
    }

    /**
     *  白字
     */
    fun setWhiteStyle() {
        tv_title.setTextColor(getcolor(R.color.white))
        iv_back.setImageResource(R.drawable.ic_youbo_goods_retrun)
        tv_right.setTextColor(getcolor(R.color.white))
    }

    /**
     * 透明风格
     */
    @Deprecated("废弃")
    fun setTransparentStyle2() {
        tv_title.setTextColor(getcolor(R.color.c_111))
        iv_back.setImageResource(R.drawable.img_titlebar_back)
        view.helper.setBackgroundColorNormal(getcolor(R.color.transparent))
    }

    /**
     * 透明风格
     */
    @Deprecated("废弃")
    fun setTransparentStyle() {
        tv_title.setTextColor(getcolor(R.color.color_ecc498))
        iv_back.setImageResource(R.drawable.img_titlebar_back_white_live)
        view.helper.setBackgroundColorNormal(getcolor(R.color.transparent))
    }

    /**
     * 左图标
     *
     * @param id
     */
    fun setLeftImageSrc(id: Int): ImageView {
        iv_back.visibility = View.VISIBLE
        iv_back.setImageResource(id)
        return iv_back
    }

    fun setRightImageSrc(id: Int): ImageView {
        fl_right.visibility = View.VISIBLE
        iv_right.setImageResource(id)
        return iv_right
    }

    fun setRightText(text: String): TextView {
        tv_right.visibility = View.VISIBLE
        tv_right.text = text
        return tv_right
    }

    fun setOnLeftImageListener(listener: View.OnClickListener) {
        fl_back.setOnClickListener(listener)
    }

    fun setOnLeftImageListener(listener: () -> Unit) {
        fl_back.setOnClickListener { listener() }
    }

    fun setRightClick(text: String, listener: () -> Unit) {
        if (text != null) {
            tv_right.visibility = View.VISIBLE
            tv_right.text = text
            tv_right.setOnClickListener { listener() }
        }
    }

    fun setRightClick(@DrawableRes res: Int, listener: () -> Unit) {
        if (res != null) {
            fl_right.visibility = View.VISIBLE
            iv_right.visibility = View.VISIBLE
            iv_right.setImageResource(res)
            fl_right.setOnClickListener { listener() }
        }
    }

    fun setRightClick(@DrawableRes res: Int, padding: Int, listener: () -> Unit) {
        if (res != null) {
            fl_right.visibility = View.VISIBLE
            iv_right.visibility = View.VISIBLE
            iv_right.setImageResource(res)
            iv_right.setPadding(padding, padding, padding, padding)
            fl_right.setOnClickListener { listener() }
        }
    }

    fun showBack() {
        fl_back.visibility = View.VISIBLE
    }

    fun hideBack() {
        fl_back.visibility = View.INVISIBLE
    }

    fun hideRight() {
        fl_right.visibility = View.INVISIBLE
        tv_right.visibility = View.INVISIBLE
    }

    fun showRight() {
        fl_right.visibility = View.VISIBLE
        tv_right.visibility = View.VISIBLE
    }

    fun hideRightIV() {
        fl_right.visibility = View.INVISIBLE
    }

    fun showRightIV() {
        fl_right.visibility = View.VISIBLE
    }

    fun hideRightTV() {
        tv_right.visibility = View.INVISIBLE
    }

    fun showRightTV() {
        tv_right.visibility = View.VISIBLE
    }
}
