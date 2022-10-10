@file:kotlin.jvm.JvmMultifileClass
@file:kotlin.jvm.JvmName("TopFun")

package a.tlib.utils

import a.tlib.base.BaseTActivity
import android.app.Activity
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter

/**
 * @author 田桂森 2021/7/21 0021
 * 自定义顶层函数
 */

/**
 * 普通带参数跳转
 */
fun open(path: String, func: Postcard.() -> Unit = {}) {
    ARouter.getInstance().build(path).apply(func).navigation()
}

/**
 * 需要返回数据的跳转，code默认10
 */
fun Activity.openForForResult(path: String, requestCode: Int = BaseTActivity.REQUEST_CODE, func: Postcard.() -> Unit = {}) {
    ARouter.getInstance().build(path).apply(func).navigation(this,requestCode)
}
