package a.tlib.utils

import android.content.Context
import android.graphics.Paint
import android.view.View
import android.widget.*
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.scwang.smartrefresh.layout.SmartRefreshLayout

fun View.gone(boolean: Boolean = true) {
    visibility = if (boolean) View.GONE else View.VISIBLE
}

fun View.show(boolean: Boolean = true) {
    visibility = if (boolean) View.VISIBLE else View.INVISIBLE
}

/**
 * true显示，false gone
 */
fun View.showGone(boolean: Boolean = true) {
    visibility = if (boolean) View.VISIBLE else View.GONE
}

fun View.hide(boolean: Boolean = true) {
    visibility = if (boolean) View.INVISIBLE else View.VISIBLE
}

fun View.isShow() = visibility == View.VISIBLE
fun View.isHide() = visibility == View.INVISIBLE || visibility == View.GONE

/**
 * 光标移动到最后
 */
fun EditText.lastSelection() {
    try {
        this.setSelection(this.length())
    } catch (e: Exception) {
    }
}

/**
 * textview添加中划线
 */
fun TextView.addStrike() {
    getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
}

/**
 * 判断EditText和TextView内容是空,EditText是TextView的子类，都能使用
 */
fun TextView.isEmpty(): Boolean {
    return this.text.isEmpty()
}

/**
 * 判断EditText和TextView内容不是空,EditText是TextView的子类，都能使用
 */
fun TextView.isNotEmpty(): Boolean {
    return this.text.isNotEmpty()
}

val TextView.string get() = this.text.toString()
/**
 * 动态设置View的padding，pt
 */
fun View.setPaddingPT(left: Int, top: Int, right: Int, bottom: Int) {
    setPadding(AutoSizeUtil.pt2px(left.toFloat()), AutoSizeUtil.pt2px(top.toFloat()), AutoSizeUtil.pt2px(right.toFloat()), AutoSizeUtil.pt2px(bottom.toFloat()))
}

/**
 * 动态设置View的margin，pt
 */
fun View.setMarginPT(horizontal: Int, vertical: Int) {
    setMarginPT(horizontal, vertical, horizontal, vertical)
}

/**
 * 动态设置View的margin，pt
 */
fun View.setMarginPT(left: Int, top: Int, right: Int, bottom: Int) {
    val params = layoutParams
    if (params is LinearLayout.LayoutParams) {
        params.setMargins(AutoSizeUtil.pt2px(left.toFloat()), AutoSizeUtil.pt2px(top.toFloat()), AutoSizeUtil.pt2px(right.toFloat()), AutoSizeUtil.pt2px(bottom.toFloat()))
    } else if (params is RelativeLayout.LayoutParams) {
        params.setMargins(AutoSizeUtil.pt2px(left.toFloat()), AutoSizeUtil.pt2px(top.toFloat()), AutoSizeUtil.pt2px(right.toFloat()), AutoSizeUtil.pt2px(bottom.toFloat()))
    } else if (params is FrameLayout.LayoutParams) {
        params.setMargins(AutoSizeUtil.pt2px(left.toFloat()), AutoSizeUtil.pt2px(top.toFloat()), AutoSizeUtil.pt2px(right.toFloat()), AutoSizeUtil.pt2px(bottom.toFloat()))
    } else if (params is SmartRefreshLayout.LayoutParams) {
        params.setMargins(AutoSizeUtil.pt2px(left.toFloat()), AutoSizeUtil.pt2px(top.toFloat()), AutoSizeUtil.pt2px(right.toFloat()), AutoSizeUtil.pt2px(bottom.toFloat()))
    }
    layoutParams = params
}

/**
 * 动态设置View的宽高
 * -1 MATCH_PARENT
 * -2 WRAP_CONTENT
 */
fun View.setWidthHeight(width: Int, height: Int) {
    val params = layoutParams
    if (params is LinearLayout.LayoutParams || params is RelativeLayout.LayoutParams ||
            params is FrameLayout.LayoutParams || params is SmartRefreshLayout.LayoutParams) {
        if (width < 0) {
            params.width =width
        } else {
            params.width = AutoSizeUtil.pt2px(width.toFloat())
        }
        if (height < 0) {
            params.height =height
        } else {
            params.height = AutoSizeUtil.pt2px(height.toFloat())
        }
    }
    layoutParams = params
}

/**
 * 双击间隔时间
 */
const val INTERVAL = 400

/**
 * 防重复点击
 */
fun View.setSingClick(function: (View) -> Unit): View {
    var temTime: Long = 0
    this.setOnClickListener {
        if (System.currentTimeMillis() - temTime > INTERVAL) {
            temTime = System.currentTimeMillis()
            function(it)
        }
    }
    return this
}

/**
 * 防重复点击，多个控件共用一个监听使用
 */
fun setSingClick(vararg views: View, function: (View) -> Unit) {
    var temTime: Long = 0
    val clickLis = object : View.OnClickListener {
        override fun onClick(v: View) {
            if (System.currentTimeMillis() - temTime > INTERVAL) {
                temTime = System.currentTimeMillis()
                function(v)
            }
        }
    }
    for (view in views) {
        view.setOnClickListener(clickLis)
    }
}

/**
 * 加载图片
 */
fun ImageView?.load(ctx: Context? = null, url: Any? = null, ro: RequestOptions? = null) {
    if (ctx != null && this != null) {
        val requestBuilder = Glide.with(ctx).load(url)
        if (ro != null) {
            requestBuilder.apply(ro)
        }
        requestBuilder.into(this)
    }
}

fun ViewPager.setPageSelectLis(function: (Int) -> Unit){
    addOnPageChangeListener(object :ViewPager.SimpleOnPageChangeListener(){
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            function(position)
        }
    })
}