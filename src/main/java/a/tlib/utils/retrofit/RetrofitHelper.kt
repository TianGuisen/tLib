package a.tlib.utils.retrofit

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ObservableSubscribeProxy
import com.uber.autodispose.SingleSubscribeProxy
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 绑定生命周期，并回调在子线程
 * 如果不关心回调就用这个
 */
fun <T> Single<T>.bindIo(life: LifecycleOwner): SingleSubscribeProxy<T> {
    return subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(life, Lifecycle.Event.ON_DESTROY)))
}

/**
 * 绑定生命周期，并回调在子线程
 * 如果不关心回调就用这个
 */
fun <T> Observable<T>.bindIo(life: LifecycleOwner): ObservableSubscribeProxy<T> {
    return subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(life, Lifecycle.Event.ON_DESTROY)))
}

/**
 * 绑定生命周期，并回调在主线程
 */
fun <T> Single<T>.bindMain(life: LifecycleOwner): SingleSubscribeProxy<T> {
    return subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(life, Lifecycle.Event.ON_DESTROY)))
}

/**
 * 绑定生命周期，并回调在主线程
 */
fun <T> Observable<T>.bindMain(life: LifecycleOwner): ObservableSubscribeProxy<T> {
    return subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(life, Lifecycle.Event.ON_DESTROY)))
}


fun <T> Single<T>.ioMain(): Single<T> {
    return subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.ioIo(): Single<T> {
    return subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
}