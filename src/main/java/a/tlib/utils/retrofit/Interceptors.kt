
import a.tlib.BuildConfig
import a.tlib.LibApp
import a.tlib.utils.AppUtil
import a.tlib.utils.encrypt.MD5Util
import a.tlib.utils.encrypt.MD5Util.TimeDifference
import a.tlib.utils.retrofit.HostConfig
import a.tlib.utils.sp
import android.net.Uri
import com.orhanobut.logger.Printer
import com.orhanobut.logger.YLog2
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okio.Buffer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.URLDecoder
import java.nio.charset.Charset
import java.util.*


object Interceptors {
    /**
     * Logger网络请求日志的tag
     */
    const val LOGGER_NET_TAG = "retrofit"
    var token = sp.getString("sid", "")!!

    /**
     * host切换拦截器
     * @type 0是普通请求，1是直播请求
     */
    class HostInterceptor(val type: Int) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val oldHttpUrl = request.url
            val builder = request.newBuilder()
            val newBaseUrl: HttpUrl?
            newBaseUrl = when (type) {
                1 -> {
                    HostConfig.liveHost.toHttpUrlOrNull()
                }
                2 -> {
                    HostConfig.newHost.toHttpUrlOrNull()
                }
                else -> request.url
            }
            newBaseUrl?.run {
                val newHttpUrl = oldHttpUrl
                        .newBuilder()
                        .scheme(scheme)
                        .host(host)
                        .port(port)
                        .build()
                return chain.proceed(builder.url(newHttpUrl).build())
            }
            return chain.proceed(request)
        }
    }

    /**
     * 参数拦截器
     */
    class ParamInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Platform", "Android_ALL")
                    .addHeader("Version", AppUtil.getVersionName())
                    .addHeader("Timestamp", (System.currentTimeMillis() / 1000 - TimeDifference).toString())
                    .addHeader("Device", AppUtil.deviceId)
                    .addHeader("appType", LibApp.appType)
                    .method(originalRequest.method, originalRequest.body)
            if (token.isNotEmpty()) {  //统一将token 传入
                requestBuilder.addHeader("token", token)
            }
            val request = requestBuilder.build()
            if (request.method.equals("POST") && request.body is FormBody) {  // post 请求数据拦截 ，将数据添加加密参数
                return chain.proceed(request.newBuilder().url(request.url.toString()).post(sortMap(request.body!!).build()).build())
            }
            return chain.proceed(request)
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
                // chain.proceed开始请求
                response = chain.proceed(orgRequest)
            } catch (e: Exception) {
                e.message?.let {
                    YLog2.t(LOGGER_NET_TAG).e(it)
                }
            } finally {
                //finally中不论如何也会打印请求信息
                val body: RequestBody? = orgRequest.body
                val buffer = Buffer()
                body?.writeTo(buffer)
                var charset = Charset.forName("UTF-8")
                val contentType = body?.contentType()
                if (contentType != null) {
                    charset = contentType.charset(Charset.forName("UTF-8"))
                }
                var requestString = StringBuffer("code=" + response?.code +
                        "|method=" + orgRequest.method +
                        "|url=" + Uri.decode(orgRequest.url.toString()) +
                        "\nheaders:" + orgRequest.headers.toMultimap())
                if (body != null) {
                    if (body is FormBody) {
                        if (body.size != 0) {
                            requestString.append("\nbody:")
                            for (i in 0 until body.size) {
                                requestString.append(body.name(i) + "=" + body.value(i) + ",")
                            }
                            requestString.delete(requestString.length - 1, requestString.length)
                        }
                    } else {
                        requestString.append("\nbody:" + buffer.readString(charset))
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
            YLog2.t(LOGGER_NET_TAG).json2(buffer2.clone().readString(charset2!!))
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


    private fun sortMap(requestBody: RequestBody): FormBody.Builder {
        val builder = FormBody.Builder()
        val stringMap = Hashtable<String, String>()
        for (i in 0 until (requestBody as FormBody).size) {
            stringMap[URLDecoder.decode(requestBody.encodedName(i).toString(), "utf-8")] = URLDecoder.decode(requestBody.encodedValue(i).toString(), "utf-8")
        }
        for ((k, v) in MD5Util.setMD5(stringMap)) {
            builder.add(k, v)
        }
        return builder
    }

}
