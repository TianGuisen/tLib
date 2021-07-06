package a.tlib.utils.retrofit

import a.tlib.bean.WXBean
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * @author 田桂森 2020/6/10 0010
 * 其他请求
 */
val otherApi: OtherApi by lazy {
    RetrofitService.createRetrofitApi(OtherApi::class.java)
}

interface OtherApi {
    /**
     * 获取微信授权
     */
    @GET("https://api.weixin.qq.com/sns/oauth2/access_token")
    fun getWXAuth(@Query("appid") appid: String, @Query("secret") secret: String, @Query("code") code: String, @Query("grant_type") grant_type: String): Single<WXBean>

    /**
     * 获取微信用户信息
     */
    @GET("https://api.weixin.qq.com/sns/userinfo")
    fun getWXInfo(@Query("access_token") access_token: String, @Query("openid") openid: String): Single<WXBean>

    /**
     * 断点下载
     *
     * @param downParam 下载参数，传下载区间使用 "bytes=" + startPos + "-"
     * @param url
     * @return
     */
    @Streaming
    @GET
    fun download(@Header("RANGE") downParam: String, @Url url: String): Observable<ResponseBody>

}