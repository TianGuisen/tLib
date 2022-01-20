package a.tlib.utils

import a.tlib.utils.abs.TTextWatcher
import a.tlib.utils.glide.GlideUtil
import a.tlib.utils.retrofit.Interceptors
import a.tlib.utils.retrofit.ResCode
import android.content.Context
import android.graphics.Paint
import android.text.Editable
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.request.RequestOptions
import com.jeremyliao.liveeventbus.LiveEventBus
import com.scwang.smartrefresh.layout.SmartRefreshLayout

fun View.gone(boolean: Boolean = true) {
    visibility = if (boolean) View.GONE else View.VISIBLE
}

/**
 * 多个view一起gone
 */
fun goneView(vararg views: View, boolean: Boolean = true) {
    views.forEach {
        it.visibility = if (boolean) View.GONE else View.VISIBLE
    }
}

fun View.show(boolean: Boolean = true) {
    visibility = if (boolean) View.VISIBLE else View.INVISIBLE
}

/**
 * 多个view一起show
 */
fun showView(vararg views: View, boolean: Boolean = true) {
    views.forEach {
        it.visibility = if (boolean) View.VISIBLE else View.INVISIBLE
    }
}

/**
 * true显示，false gone
 */
fun View.showGone(boolean: Boolean = true) {
    visibility = if (boolean) View.VISIBLE else View.GONE
}

/**
 * true显示，false gone
 */
fun showGoneView(vararg views: View, boolean: Boolean = true) {
    views.forEach {
        it.visibility = if (boolean) View.VISIBLE else View.GONE
    }
}

/**
 * true INVISIBLE，false  显示
 */
fun View.hide(boolean: Boolean = true) {
    visibility = if (boolean) View.INVISIBLE else View.VISIBLE
}

/**
 * 多个view一起hide
 */
fun hide(vararg views: View, boolean: Boolean = true) {
    views.forEach {
        it.visibility = if (boolean) View.INVISIBLE else View.VISIBLE
    }
}

/**
 * 是否是VISIBLE
 */
fun View.isShow() = visibility == View.VISIBLE

/**
 * 是否是INVISIBLE或GONE
 */
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
 * 添加文字改变后的监听
 */
fun EditText.afterTextChanged(function: (String) -> Unit) {
    addTextChangedListener(object : TTextWatcher() {
        override fun afterTextChanged(s: Editable) {
            function(s.toString())
        }
    })
}

/**
 * 添加文字改变前监听
 */
fun EditText.beforeTextChanged(function: (s: String, start: Int, count: Int, after: Int) -> Unit) {
    addTextChangedListener(object : TTextWatcher() {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            function(s.toString(), start, count, after)
        }
    })
}

/**
 * 添加文字改变中监听
 */
fun EditText.onTextChanged(function: (s: String, start: Int, before: Int, count: Int) -> Unit) {
    addTextChangedListener(object : TTextWatcher() {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            function(s.toString(), start, before, count)
        }
    })
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
    setPadding(
            AutoSizeUtil.pt2px(left.toFloat()),
            AutoSizeUtil.pt2px(top.toFloat()),
            AutoSizeUtil.pt2px(right.toFloat()),
            AutoSizeUtil.pt2px(bottom.toFloat())
    )
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
        params.setMargins(
                AutoSizeUtil.pt2px(left.toFloat()),
                AutoSizeUtil.pt2px(top.toFloat()),
                AutoSizeUtil.pt2px(right.toFloat()),
                AutoSizeUtil.pt2px(bottom.toFloat())
        )
    } else if (params is RelativeLayout.LayoutParams) {
        params.setMargins(
                AutoSizeUtil.pt2px(left.toFloat()),
                AutoSizeUtil.pt2px(top.toFloat()),
                AutoSizeUtil.pt2px(right.toFloat()),
                AutoSizeUtil.pt2px(bottom.toFloat())
        )
    } else if (params is FrameLayout.LayoutParams) {
        params.setMargins(
                AutoSizeUtil.pt2px(left.toFloat()),
                AutoSizeUtil.pt2px(top.toFloat()),
                AutoSizeUtil.pt2px(right.toFloat()),
                AutoSizeUtil.pt2px(bottom.toFloat())
        )
    } else if (params is SmartRefreshLayout.LayoutParams) {
        params.setMargins(
                AutoSizeUtil.pt2px(left.toFloat()),
                AutoSizeUtil.pt2px(top.toFloat()),
                AutoSizeUtil.pt2px(right.toFloat()),
                AutoSizeUtil.pt2px(bottom.toFloat())
        )
    }
    layoutParams = params
}

/**
 * 动态设置View的宽高
 * -1 MATCH_PARENT
 * -2 WRAP_CONTENT
 * null不进行设置
 */
fun View.setWidthHeight(widthPT: Int? = null, heightPT: Int? = null) {
    val params = layoutParams
    if (params is LinearLayout.LayoutParams || params is RelativeLayout.LayoutParams ||
            params is FrameLayout.LayoutParams || params is SmartRefreshLayout.LayoutParams
    ) {
        if (widthPT != null) {
            if (width < 0) {
                params.width = widthPT
            } else {
                params.width = AutoSizeUtil.pt2px(widthPT.toFloat())
            }
        }
        if (heightPT != null) {
            if (height < 0) {
                params.height = heightPT
            } else {
                params.height = AutoSizeUtil.pt2px(heightPT.toFloat())
            }
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
 * 防重复点击,需要登录
 */
fun View.setLoginSingClick(function: (View) -> Unit): View {
    var temTime: Long = 0
    this.setOnClickListener {
        if (Interceptors.token.isEmpty()) {
            LiveEventBus.get(ResCode.TOKEN_OVERDUE.toString()).post(null)
            return@setOnClickListener
        }
        if (System.currentTimeMillis() - temTime > INTERVAL) {
            temTime = System.currentTimeMillis()
            function(it)
        }
    }
    return this
}

/**
 * 防重复点击,可设置点击间隔
 */
fun View.setSingClick(interval: Int, function: (View) -> Unit): View {
    var temTime: Long = 0
    this.setOnClickListener {
        if (System.currentTimeMillis() - temTime > interval) {
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
    GlideUtil.load(ctx, url, this, ro)
}

/**
 * 加载图片
 */
fun ImageView?.load(ctx: Fragment? = null, url: Any? = null, ro: RequestOptions? = null) {
    GlideUtil.load(ctx, url, this, ro)
}

fun ViewPager.setPageSelectLis(function: (Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            function(position)
        }
    })
}