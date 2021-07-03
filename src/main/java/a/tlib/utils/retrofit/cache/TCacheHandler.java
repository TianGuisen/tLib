package a.tlib.utils.retrofit.cache;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import a.tlib.LibApp;
import a.tlib.utils.ACache;
import a.tlib.utils.gson.GsonUtil;
import a.tlib.utils.retrofit.CacheCall;
import a.tlib.utils.retrofit.rxjava2Adapter.RxJava2CallAdapter;
import io.reactivex.Single;
import retrofit2.Retrofit;

/**
 * @author 田桂森 2021/7/3 0003
 * 缓存代理
 */
public class TCacheHandler<T> implements InvocationHandler {
    private Retrofit retrofit;
    private T api;
    
    public TCacheHandler(Retrofit retrofit, T api) {
        this.retrofit = retrofit;
        this.api = api;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        TCache cache = method.getAnnotation(TCache.class);
        //是否有缓存的注解
        if (cache != null) {
            //获取缓存
            String key = "";
            for (Object arg : args) {
                key = key + arg.toString();
            }
            String cacheStr = ACache.get(LibApp.app).getAsString(getCacheKey(method, args));
            if (cacheStr != null && !cacheStr.isEmpty()) {
                RxJava2CallAdapter callAdapter = (RxJava2CallAdapter) retrofit.nextCallAdapter(null, method.getGenericReturnType(), method.getAnnotations());
                //获取返回类型
                Type responseType = callAdapter.responseType();
                //解析成对象
                Object responseObj = GsonUtil.gson.fromJson(cacheStr, responseType);
                CacheCall call = new CacheCall(responseObj);
                return callAdapter.adapt(call);
            }
        }
        try {
            //接口方法的返回类型应该都是Single，直接强转
            Single ob = (Single) method.invoke(api, args);
            return ob.doOnSuccess(o -> {
                //请求成功后缓存
                if (cache != null) {
                    ACache.get(LibApp.app).put(getCacheKey(method, args), GsonUtil.toJson(o), cache.value());
                }
            });
        } catch (Exception e) {
            return method.invoke(api, args);
        }
    }
    
    
    public String getCacheKey(Method method, Object[] args) {
        StringBuffer key = new StringBuffer(method.toGenericString());
        //拼接参数
        for (Object arg : args) {
            key.append(arg.toString()).append(",");
        }
        return key.toString();
    }
}