package a.tlib.utils.retrofit

import a.tlib.BuildConfig
import a.tlib.LibApp
import a.tlib.utils.gson.GsonUtil.gson
import a.tlib.utils.retrofit.cache.TCacheHandler
import a.tlib.utils.retrofit.converter.FileConverterFactory
import a.tlib.utils.retrofit.rxjava2Adapter.RxJava2CallAdapterFactory
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Proxy
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*


object RetrofitService {
    //必须要随便写个url，否则微信接口会失败
    const val BASE_URL = "http://youbao.youboe.com/"
    val baseParamInterceptor by lazy {
        Interceptors.BaseParamInterceptor()
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
            .collector(ChuckerCollector(
                    context = LibApp.app,
                    showNotification = true,
                    retentionPeriod = RetentionManager.Period.ONE_HOUR
            ))
            // 最大正文内容长度(以字节为单位)，在此之后，响应将被截断。
            .maxContentLength(250_000L)
            .alwaysReadResponseBody(true)
            .build()

    /**
     * 创建有缓存的api
     */
    @JvmStatic
    fun <T> createRetrofitApi(clazz: Class<T>, vararg interceptors: Interceptor): T {
        val builder = OkHttpClient.Builder()
        for (interceptor in interceptors) {
            builder.addInterceptor(interceptor)
        }
        builder.addInterceptor(chuckerInterceptor)//通知栏日志
        builder.addInterceptor(loggerInterceptor)//日志打印
        if (BuildConfig.IS_DEBUG) {//debug包时解除https抓包限制
            builder.sslSocketFactory(createSSLSocketFactory(), TrustAllCerts())
                    .hostnameVerifier(TrustAllHostnameVerifier())
        }
        builder.retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(fileConverterFactory)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build())
                .build()
        val api = retrofit.create(clazz)
        return Proxy.newProxyInstance(clazz.getClassLoader(), arrayOf<Class<*>>(clazz), TCacheHandler(retrofit, api)) as T
    }

    /**
     * 协程用
     */
    @JvmStatic
    fun <T> createRetrofitApi2(clazz: Class<T>, vararg interceptors: Interceptor): T {
        val builder = OkHttpClient.Builder()
        for (interceptor in interceptors) {
            builder.addInterceptor(interceptor)
        }
        builder.addInterceptor(chuckerInterceptor)//通知栏日志
        builder.addInterceptor(loggerInterceptor)//日志打印
        if (BuildConfig.IS_DEBUG) {//debug包时解除https抓包限制
            builder.sslSocketFactory(createSSLSocketFactory(), TrustAllCerts())
                    .hostnameVerifier(TrustAllHostnameVerifier())
        }
        builder.retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(fileConverterFactory)
                .addConverterFactory(gsonConverterFactory)
                .client(builder.build())
                .build()
        return retrofit.create(clazz)
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