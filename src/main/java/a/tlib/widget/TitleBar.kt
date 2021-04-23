package a.tlib.widget

import a.tlib.LibApp
import a.tlib.R
import a.tlib.utils.getcolor
import a.tlib.utils.isNotNullEmpty
import a.tlib.utils.show
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
    lateinit var tv_title: TextView
    lateinit var fl_back: FrameLayout
    lateinit var iv_back: ImageView
    lateinit var iv_right: ImageView
    lateinit var fl_right: FrameLayout
    lateinit var tv_right: RTextView
    lateinit var view_line: View

    constructor(context: Context) : super(context, null) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    
    open fun init() {
        view = RView.inflate(context, R.layout.view_title, this) as TitleBar
        view.helper.setBackgroundColorNormal(getcolor(R.color.black))
        tv_title = view.findViewById(R.id.tv_title)
        fl_back = view.findViewById(R.id.fl_back)
        iv_back = view.findViewById(R.id.iv_back)
        iv_right = view.findViewById(R.id.iv_right)
        fl_right = view.findViewById(R.id.fl_right)
        tv_right = view.findViewById(R.id.tv_right)
        view_line = view.findViewById(R.id.view_line)
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
        tv_title.setTextColor(getcolor(R.color.c_111))
        iv_back.setImageResource(R.drawable.img_titlebar_back)
        view.helper.setBackgroundColorNormal(getcolor(R.color.white))
        tv_right.setTextColor(getcolor(R.color.c_111))
    }

    /**
     * 透明风格
     */
    fun setTransparentStyle2() {
        tv_title.setTextColor(getcolor(R.color.c_111))
        iv_back.setImageResource(R.drawable.img_titlebar_back)
        view.helper.setBackgroundColorNormal(getcolor(R.color.translucent))
    }

    /**
     * 透明风格
     */
    fun setTransparentStyle() {
        tv_title.setTextColor(getcolor(R.color.color_ecc498))
        iv_back.setImageResource(R.drawable.img_titlebar_back_white_live)
        view.helper.setBackgroundColorNormal(getcolor(R.color.translucent))
    }

    fun showLine() {
        view_line.show()
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
