package a.tlib.utils.retrofit

import okhttp3.Interceptor

/**
 * 网络请求tag管理
 * 如果设置了@Headers(ApiTagManager.REPEAT_CLOSE_AFTER)
            @Headers(ApiTagManager.REPEAT_CLOSE_BEFORE)
 * 那么相同tag的请求只会存在一个
 */
object ApiTagManager {
    /**
     * 关闭后入队的请求,比较常用:比如按钮的防重复时间是500,但是点击按钮后后台处理时间长达1000,在后500时间内按钮是可点击的但是请求是无意义的
     */
    const val REPEAT_CLOSE_AFTER = "repeat:close_after"

    /**
     * 关闭先入队的请求,场景很少:频繁调用接口并只采用最后一次请求的数据
     */
    const val REPEAT_CLOSE_BEFORE = "repeat:close_before"

    const val REPEAT_KEY = "repeat"

    const val REPEAT_VALUE_CLOSE_AFTER = "close_after"
    const val REPEAT_VALUE_CLOSE_BEFORE = "close_before"

    @JvmStatic
     val chainMap = mutableMapOf<String, Interceptor.Chain>()

    @JvmStatic
    fun add1(tag: String, chain: Interceptor.Chain) {
        chainMap.keys.forEach {
            if (tag == it) {
                chain.call().cancel()
                return
            }
        }
        chainMap.put(tag, chain)
    }

    @JvmStatic
    fun add2(tag: String, chain: Interceptor.Chain) {
        if (tag == null) {
            throw IllegalArgumentException("tag不能为null")
        }
        if (chainMap.isEmpty()) {
            return
        }
        val m1 = chainMap.get(tag)
        if (m1 == null) {
            return
        }
        if (!chain.call().isCanceled()) {
            chain.call().cancel()
            chainMap.remove(tag)
        }
        chainMap.put(tag, chain)
    }
    
    @JvmStatic
    fun remove2(tag: String?) {
        if (tag == null) {
            return
        }
        if (!chainMap.isEmpty()) {
            chainMap.remove(tag)
        }
    }
}