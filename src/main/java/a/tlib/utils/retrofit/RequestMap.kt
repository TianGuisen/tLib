package a.tlib.utils.retrofit

/**
 * @author 田桂森 2019/5/17
 * 用于@FieldMap请求
 */
class RequestMap {

    companion object {
        fun create() = RequestMap()
    }

    private val map = mutableMapOf<String, Any>()

    /**
     * 自动过滤null字段
     */
    fun put(key: String, value: Any?): RequestMap {
        value?.let {
            map.put(key, it)
        }
        return this
    }

     fun get(key: String): Any? {
        return map.get(key)
    }

    fun build() = map
}