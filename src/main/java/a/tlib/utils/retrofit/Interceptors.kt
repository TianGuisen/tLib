package a.tlib.utils.retrofit

import a.tlib.logger.Printer
import a.tlib.logger.YLog2
import a.tlib.utils.*
import android.net.Uri
import okhttp3.*
import okio.Buffer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.Charset


object Interceptors {
    /**
     * Logger网络请求日志的tag
     */
    const val LOGGER_NET_TAG = "retrofit"

    var token = sp.getString("sid", "")!!

    /**
     * 时间戳
     */
    @JvmField
    var TimeDifference: Long = 0

    @JvmField
    val appKay = "Android_1.0"

    @JvmField
    val appSecret = "9edd61b7c87b3a556def3e4ce073caf6"

    /**
     * 参数拦截器，如果有其他额外单独的请球头，就继承他，重写addHeaders方法
     *
     */
    open class BaseParamInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Platform", "Android_ALL")
                    .addHeader("Version", AppUtil.getVersionName())
                    .addHeader("Timestamp", (System.currentTimeMillis() / 1000 - TimeDifference).toString())
                    .addHeader("Device", AppUtil.deviceId)
                    .removeHeader(ApiTagManager.REPEAT_KEY)
                    .method(originalRequest.method, originalRequest.body)
            if (token.isNotEmpty()) {
                requestBuilder.addHeader("token", token)
            }
            //添加子类其他的请求头
            addHeaders(requestBuilder)
            var request = requestBuilder.build()
            if (request.method.equals("POST") && request.body is FormBody) { 
            // post 请求数据拦截 ，将数据添加加密参数
//                request = addPostParams(request)
            } 
            return chain.proceed(request)
        }

        open fun addHeaders(requestBuilder: Request.Builder) {

        }
    }

    /**
     * 日志拦截器
     */
    class LoggerInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val orgRequest = chain.request()
            var response: Response? = null
            try {
                //获取全部请求头，处理重复请求
                val headers = orgRequest.headers
                when (headers.get(ApiTagManager.REPEAT_KEY)) {
                    ApiTagManager.REPEAT_VALUE_CLOSE_AFTER -> {
                        ApiTagManager.addAfterRepeat(Uri.decode(orgRequest.url.toString()), chain)
                    }
                    ApiTagManager.REPEAT_VALUE_CLOSE_BEFORE -> {
                        ApiTagManager.addBeforeRepeat(Uri.decode(orgRequest.url.toString()), chain)
                    }
                }
                // chain.proceed开始请求
                response = chain.proceed(orgRequest)
            } catch (e: Exception) {
                e.message?.let {
                    YLog2.t(LOGGER_NET_TAG).e(orgRequest.url.toString() + it)
                }
            } finally {
                //移除
                ApiTagManager.remove2(Uri.decode(orgRequest.url.toString()))
                //finally中不论如何也会打印请求信息
                val body: RequestBody? = orgRequest.body
                val buffer = Buffer()
                body?.writeTo(buffer)
                var requestString = StringBuffer(
                        "code=" + response?.code +
                                "|method=" + orgRequest.method +
                                "|url=" + Uri.decode(orgRequest.url.toString()) +
                                "\nheaders:" + orgRequest.headers.toMultimap()
                )
                if (body != null) {
                    if (body is FormBody) {
                        if (body.size != 0) {
                            requestString.append("\nbody:")
                            for (i in 0 until body.size) {
                                requestString.append(body.name(i) + "=" + body.value(i) + ",")
                            }
                            requestString.delete(requestString.length - 1, requestString.length)
                        }
                    } else if (body is MultipartBody) {//是图片文件之类，只打印名字等简单信息
                        requestString.append("\nbody:")
                        body.parts.forEach {
                            requestString.append(it.headers)
                        }
                    }
                }
                //打印请求json
                YLog2.t(LOGGER_NET_TAG).d(requestString)
            }
            val responseBody = response!!.body
            val source = responseBody!!.source()
            source.request(Long.MAX_VALUE)
            val buffer2 = source.buffer()
            val contentType2 = responseBody.contentType()
            val charset2 = contentType2?.charset(Charset.forName("UTF-8"))
            //json日志使用鼠标中键进行选中
            //打印返回json
            val jsonString = buffer2.clone().readString(charset2!!)
            YLog2.t(LOGGER_NET_TAG).json2(jsonString)
            return response
        }
    }

    fun Printer.json2(json: String?) {
        if (json.isNullOrEmpty()) {
            d("Empty/Null json content")
            return
        }
        try {
            val json = json.trim { it <= ' ' }
            if (json.startsWith("{")) {
                val jsonObject = JSONObject(json)
                val message = jsonObject.toString(2)
                //日志打印不全的时候debug这里复制全部数据
                d(message)
                return
            }
            if (json.startsWith("[")) {
                val jsonArray = JSONArray(json)
                val message = jsonArray.toString(2)
                d(message)
                return
            }
            e(json)
        } catch (e: JSONException) {
            e(json)
        }
    }

}
