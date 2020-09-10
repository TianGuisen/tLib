package a.tlib.utils

import android.content.Context
import android.graphics.Paint
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun View.gone() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.isShow() = visibility == View.VISIBLE
fun View.isHide() = visibility == View.INVISIBLE || visibility == View.GONE


/**
 * textview添加中划线
 */
fun TextView.addStrike() {
    getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
}

fun EditText.isEmpty(): Boolean {
    return this.text.isEmpty()
}

val EditText.string get() = this.text.toString()


val TextView.string get() = this.text.toString()

/**
 * 双击间隔时间
 */
const val INTERVAL = 400

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


fun ImageView?.load(ctx: Context? = null, url: Any? = null, ro: RequestOptions? = null) {
    if (ctx != null && this != null) {
        val requestBuilder = Glide.with(ctx).load(url)
        if (ro != null) {
            requestBuilder.apply(ro)
        }
        requestBuilder.into(this)
    }
}
//inline fun singClick(crossinline function: (View) -> Unit): View.OnClickListener {
//    var temTime: Long = 0
//    return object : View.OnClickListener {
//        override fun onClick(v: View) {
//            if (System.currentTimeMillis() - temTime > INTERVAL) {
//                temTime = System.currentTimeMillis()
//                function(v)
//            }
//        }
//    }
//}

//
//inline fun refreshLis(crossinline refreshLayout: (RefreshLayout) -> Unit): OnRefreshListener {
//    return object : OnRefreshListener {
//        override fun onRefresh(refreshLayout: RefreshLayout) {
//            refreshLayout(refreshLayout)
//        }
//    }
//}
//
//inline fun loadMoreLis(crossinline refreshLayout: (RefreshLayout) -> Unit): OnLoadMoreListener {
//    return object : OnLoadMoreListener {
//        override fun onLoadMore(refreshLayout: RefreshLayout) {
//            refreshLayout(refreshLayout)
//        }
//    }
//}





