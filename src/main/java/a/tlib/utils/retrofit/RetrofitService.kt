package com.lb.baselib.retrofit

import Interceptors
import a.tlib.BuildConfig
import a.tlib.utils.gson.GsonUtil.gson
import a.tlib.utils.retrofit.converter.FileConverterFactory
import a.tlib.utils.retrofit.rxjava2Adapter.RxJava2CallAdapterFactory
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
    var baseUrl = ""
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