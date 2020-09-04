package com.lb.baselib.retrofit

/**
 * @author 田桂森 2019/5/17
 * 用于@FileMap请求
 */
class RequestMap {

    companion object {
        fun create() = RequestMap()
    }

    private val map = mutableMapOf<String, Any>()
    fun put(key: String, value: Any?): RequestMap {
        value?.let {
            map.put(key, it)
        }
        return this
    }

    fun build() = map
}