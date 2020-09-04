package com.lb.baselib.retrofit

import Interceptors
import a.tlib.utils.gson.GsonUtil.gson
import a.tlib.utils.retrofit.HostConfig.newHost
import a.tlib.utils.retrofit.rxjava2Adapter.RxJava2CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitService {

    /**
     * 直播的请求
     */
    val retrofitLive: Retrofit by lazy {
        val retrofitParams = RetrofitParams()
        retrofitParams.interceptors.add(Interceptors.HostInterceptor(1))
        retrofitParams.interceptors.add(Interceptors.ParamInterceptor())
        retrofitParams.interceptors.add(Interceptors.LoggerInterceptor())
        retrofitParams.converterFactory = GsonConverterFactory.create(gson)
        createRetrofit(retrofitParams)
    }
    /**
     * 其他请求
     */
    val retrofitOther: Retrofit by lazy {
        val retrofitParams = RetrofitParams()
        retrofitParams.interceptors.add(Interceptors.ParamInterceptor())
        retrofitParams.interceptors.add(Interceptors.LoggerInterceptor())
        retrofitParams.converterFactory = GsonConverterFactory.create(gson)
        createRetrofit(retrofitParams)
    }
    /**
     * new
     */
    val retrofitNew: Retrofit by lazy {
        val retrofitParams = RetrofitParams()
        retrofitParams.interceptors.add(Interceptors.HostInterceptor(2))
        retrofitParams.interceptors.add(Interceptors.ParamInterceptor())
        retrofitParams.interceptors.add(Interceptors.LoggerInterceptor())
        retrofitParams.converterFactory = GsonConverterFactory.create(gson)
        createRetrofit(retrofitParams)
    }
    private fun createRetrofit(params: RetrofitParams): Retrofit {
        val builder = OkHttpClient.Builder()
        for (interceptor in params.interceptors) {
            builder.addInterceptor(interceptor)
        }
        builder.retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
        val retrofit = Retrofit.Builder()
                .baseUrl(params.baseUrl)
                .addConverterFactory(params.converterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build())
                .build()
        return retrofit
    }

    private class RetrofitParams {
        val interceptors = mutableListOf<Interceptor>()
        var baseUrl = newHost
        lateinit var converterFactory: Converter.Factory
    }
}