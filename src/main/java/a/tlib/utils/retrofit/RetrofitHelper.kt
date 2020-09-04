package com.lb.baselib.retrofit

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
 * 绑定生命周期，并回调在子线程
 */
fun <T> Observable<T>.bindMain(life: LifecycleOwner): ObservableSubscribeProxy<T> {
    return subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(life, Lifecycle.Event.ON_DESTROY)))
}
///**
// * 订阅在io,回调在main
// * 传入fragment会绑定生命周期在destroy的时候取消订阅
// * 传入actvity会绑定生命周期在destroy的时候取消订阅
// */
//fun <T> ioMain(life: LifecycleProvider<*>): SingleTransformer<T, T> {
//    return object : SingleTransformer<T, T> {
//        @SuppressLint("CheckResult")
//        override fun apply(upstream: Single<T>): SingleSource<T> {
//            if (life is BaseFragment) {
//                upstream.compose(life.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
//            } else if (life is BaseActivity) {
//                upstream.compose(life.bindUntilEvent(ActivityEvent.DESTROY))
//            }
//            return upstream.subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//
//        }
//    }
//}
//
///**
// * 订阅在io,回调在io
// * 传入fragment会绑定生命周期在destroy的时候取消订阅
// * 传入actvity会绑定生命周期在destroy的时候取消订阅
// */
//fun <T> ioIo(life: LifecycleProvider<*>): SingleTransformer<T, T> {
//    return object : SingleTransformer<T, T> {
//        @SuppressLint("CheckResult")
//        override fun apply(upstream: Single<T>): SingleSource<T> {
//            if (life is BaseFragment) {
//                upstream.compose(life.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
//            } else if (life is BaseActivity) {
//                upstream.compose(life.bindUntilEvent(ActivityEvent.DESTROY))
//            }
//            return upstream.subscribeOn(Schedulers.io())
//                    .observeOn(Schedulers.io())
//        }
//    }
//}


