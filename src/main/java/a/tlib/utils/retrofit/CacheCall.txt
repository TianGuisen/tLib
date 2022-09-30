package a.tlib.utils.retrofit;

import java.io.IOException;

import okhttp3.Request;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @param <T>
 * @author 田桂森 2021/7/3 0003
 * 模拟call方法，为了不走OkhttpCall
 */
public final class CacheCall<T> implements Call<T> {
    
    private T data;
    
    public CacheCall(T data) {
        this.data = data;
    }
    
    @Override
    public Response<T> execute() throws IOException {
        return Response.success(data);
    }
    
    @Override
    public void enqueue(Callback<T> callback) {
        callback.onResponse(null, Response.success(data));
    }
    
    @Override
    public boolean isExecuted() {
        return false;
    }
    
    @Override
    public void cancel() {
        
    }
    
    @Override
    public boolean isCanceled() {
        return false;
    }
    
    @Override
    public Call<T> clone() {
        return this;
    }
    
    @Override
    public Request request() {
        return null;
    }
    
    @Override
    public Timeout timeout() {
        return null;
    }
}