package a.tlib.utils.retrofit.observer;

import java.text.DecimalFormat;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * 自定义 基础功能 Observer
 * Created by fangs on 2017/8/28.
 */
public abstract class ProgressReqObserver<V> implements Observer<V> {

    private Disposable disposed;

    public ProgressReqObserver() {}

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        this.disposed = d;
    }

    @Override
    public void onNext(V t) {
        if (t instanceof Double) {
            String percent = doubleToKeepTwoDecimalPlaces(((Double) t).doubleValue());
            onProgress(percent);
        } else {
            onSuccess(t);
        }
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onComplete() {
    }


    /**
     * @param percent 进度百分比 数
     */
    protected void onProgress(String percent){

    }

    /**
     * 请求成功 回调
     *
     * @param t 请求返回的数据
     */
    protected abstract void onSuccess(V t);

    /**
     * double类型数字  保留一位小数(四舍五入)
     * DecimalFormat转换最简便
     *
     * @param doubleDigital
     * @return String
     */
    public static String doubleToKeepTwoDecimalPlaces(double doubleDigital) {
        DecimalFormat df = new DecimalFormat("##0.0");

        return df.format(doubleDigital);
    }
}
