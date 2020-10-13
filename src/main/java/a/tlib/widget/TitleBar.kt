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
    lateinit var view: TitleBar
    val tv_title by lazy { view.findViewById<TextView>(R.id.tv_title) }
    val fl_back by lazy { view.findViewById<FrameLayout>(R.id.fl_back) }
    val iv_back by lazy { view.findViewById<ImageView>(R.id.iv_back) }
    val iv_right by lazy { view.findViewById<ImageView>(R.id.iv_right) }
    val fl_right by lazy { view.findViewById<FrameLayout>(R.id.fl_right) }
    val tv_right by lazy { view.findViewById<RTextView>(R.id.tv_right) }

    constructor(context: Context) : super(context, null) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    open fun init() {
        view= RView.inflate(context,R.layout.view_title, this) as TitleBar
        view.helper.setBackgroundColorNormal(LibApp.titleBarColor)
    }

    fun setTitle(string: String?): TextView {
        if (string.isNotNullEmpty()) {
            tv_title.text = string
        }
        return tv_title
    }

    fun setTitleColor(color: Int) {
        tv_title.setTextColor(color)
    }

    fun setBarColor(color: Int) {
        view.helper.setBackgroundColorNormal(getcolor(color))
    }

    /**
     * 白色风格
     */
    fun setWhiteStyle() {
        tv_title.setTextColor(getcolor(R.color.black))
        iv_back.setImageResource(R.drawable.img_titlebar_back)
        view.helper.setBackgroundColorNormal(getcolor(R.color.white))
    }

    /**
     * 透明风格
     */
    fun setTransparentStyle() {
        tv_title.setTextColor(getcolor(R.color.color_ecc498))
        iv_back.setImageResource(R.drawable.img_titlebar_back_white_live)
        view.helper.setBackgroundColorNormal(getcolor(R.color.translucent))
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
}
