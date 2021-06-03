package a.tlib.utils.retrofit

import Interceptors
import a.tlib.BuildConfig
import a.tlib.LibApp
import a.tlib.utils.gson.GsonUtil.gson
import a.tlib.utils.retrofit.converter.FileConverterFactory
import a.tlib.utils.retrofit.rxjava2Adapter.RxJava2CallAdapterFactory
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*


object RetrofitService {
    @JvmField
    var headerMap: MutableMap<String, String>? = null

    val paramInterceptor by lazy {
        Interceptors.ParamInterceptor(headerMap)
    }

    val loggerInterceptor by lazy {
        Interceptors.LoggerInterceptor()
    }

    val gsonConverterFactory by lazy {
        GsonConverterFactory.create(gson)
    }

    val fileConverterFactory by lazy {
        FileConverterFactory()
    }
    val chuckerInterceptor = ChuckerInterceptor.Builder(LibApp.app)
            // The previously created Collector
            .collector(ChuckerCollector(
                    context = LibApp.app,
                    // Toggles visibility of the push notification
                    showNotification = true,
                    // Allows to customize the retention period of collected data
                    retentionPeriod = RetentionManager.Period.ONE_HOUR
            ))
            // 最大正文内容长度(以字节为单位)，在此之后，响应将被截断。
            .maxContentLength(250_000L)
            // List of headers to replace with ** in the Chucker UI
            // Read the whole response body even when the client does not consume the response completely.
            // This is useful in case of parsing errors or when the response body
            // is closed before being read like in Retrofit with Void and Unit types.
            .alwaysReadResponseBody(true)
            .build()


    @JvmStatic
    fun createRetrofit(params: RetrofitParams): Retrofit {
        val builder = OkHttpClient.Builder()
        for (interceptor in params.interceptors) {
            builder.addInterceptor(interceptor)
        }
        if (BuildConfig.IS_DEBUG) {//debug包时解除https抓包限制
            builder.sslSocketFactory(createSSLSocketFactory(), TrustAllCerts())
                    .hostnameVerifier(TrustAllHostnameVerifier())
        }
        builder.retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
        val retrofit = Retrofit.Builder()
                .baseUrl(params.baseUrl)
                .addConverterFactory(fileConverterFactory)
                .addConverterFactory(params.converterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build())
                .build()
        return retrofit
    }

    // 屏蔽证书
    @JvmStatic
    private fun createSSLSocketFactory(): SSLSocketFactory {
        var ssfFactory: SSLSocketFactory? = null
        try {
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, arrayOf<TrustManager>(TrustAllCerts()), SecureRandom())
            ssfFactory = sc.socketFactory
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ssfFactory!!
    }

}

class RetrofitParams {
    val interceptors = mutableListOf<Interceptor>()
    //必须要随便写个url，否则微信接口会失败
    var baseUrl = "https://www.baidu.com/"
    lateinit var converterFactory: Converter.Factory
}

private class TrustAllCerts : X509TrustManager {
    @Throws(CertificateException::class)
    override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {
    }

    @Throws(CertificateException::class)
    override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {
    }

    override fun getAcceptedIssuers(): Array<X509Certificate?> {
        return arrayOfNulls(0)
    }
}

class TrustAllHostnameVerifier : HostnameVerifier {

    override fun verify(hostname: String?, session: SSLSession?): Boolean {
        return true
    }
}