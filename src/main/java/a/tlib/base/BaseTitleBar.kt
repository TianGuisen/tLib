package a.tlib.base

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

/**
 * 基础TitleBar
 */
abstract class BaseTitleBar : RFrameLayout {

    abstract var layoutId:Int

    val tv_title by lazy { findViewById<TextView>(R.id.tv_title) }
    val fl_back by lazy { findViewById<FrameLayout>(R.id.fl_back) }
    val iv_back by lazy { findViewById<ImageView>(R.id.iv_back) }
    val iv_right by lazy { findViewById<ImageView>(R.id.iv_right) }
    val fl_right by lazy { findViewById<FrameLayout>(R.id.fl_right) }
    val tv_right by lazy { findViewById<RTextView>(R.id.tv_right) }
    

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    open fun init() {
        RView.inflate(context, layoutId, this)
    }

    fun setTitle(string: String?): TextView {
        if (string.isNotNullEmpty()) {
            tv_title.text = string
        }
        return tv_title
    }

    fun setTitleColor(color: Int) {
        tv_title.setTextColor(getcolor(color))
    }

    fun setColor(color: Int) {
        helper.setBackgroundColorNormal(getcolor(color))
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
