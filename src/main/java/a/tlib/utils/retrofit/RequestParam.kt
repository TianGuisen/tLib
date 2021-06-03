package a.tlib.utils.retrofit

import a.tlib.utils.gson.GsonUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

/**
 * @author 田桂森 2019/5/17
 * 用于@Body  json请求
 */
class RequestParam {

    companion object {
        fun create() = RequestParam()
    }
    private val map = mutableMapOf<Any, Any>()

    fun add(key: Any, value: Any?): RequestParam {
        value?.let {
            map.put(key, it)
        }
        return this
    }
    
    fun build(): RequestBody {
        val toJSON = GsonUtil.toJson(map)
        val body = RequestBody.create("application/json;charset=UTF-8".toMediaTypeOrNull(), toJSON)
        return body
    }
}