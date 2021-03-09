package com.ekoapp.rxlifecycle.extension

import android.view.View
import com.trello.rxlifecycle3.LifecycleProvider
import hu.akarnokd.rxjava.interop.RxJavaInterop
import rx.Observable

fun <E, T> Observable<T>.untilLifecycleEnd(lifecycleProvider: LifecycleProvider<E>, uniqueId: String? = null): Observable<T> {
    return RxJavaInterop.toV1Observable(
        RxJavaInterop.toV2Flowable(this)
            .untilLifecycleEnd(lifecycleProvider, uniqueId)
    )
}

fun <T> Observable<T>.untilLifecycleEnd(view: View, uniqueId: String? = null): Observable<T> {
    return RxJavaInterop.toV1Observable(
        RxJavaInterop.toV2Flowable(this)
            .untilLifecycleEnd(view, uniqueId)
    )
}