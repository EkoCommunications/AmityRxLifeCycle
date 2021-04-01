package com.ekoapp.rxlifecycle.extension

import android.view.View
import com.trello.rxlifecycle3.LifecycleProvider
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.android.FragmentEvent
import com.trello.rxlifecycle3.kotlin.bindToLifecycle
import com.trello.rxlifecycle3.kotlin.bindUntilEvent
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import java.util.concurrent.CancellationException

@Suppress("UNCHECKED_CAST")
inline fun <reified E, T> Single<T>.untilLifecycleEnd(lifecycleProvider: LifecycleProvider<E>, uniqueId: String? = null): Single<T> {
    return when (E::class) {
        ActivityEvent::class -> bindUntilEvent(
            lifecycleProvider as LifecycleProvider<ActivityEvent>,
            ActivityEvent.DESTROY
        )
        FragmentEvent::class -> bindUntilEvent(
            lifecycleProvider as LifecycleProvider<FragmentEvent>,
            FragmentEvent.DESTROY
        )
        ViewEvent::class -> bindUntilEvent(
            lifecycleProvider as LifecycleProvider<ViewEvent>,
            ViewEvent.DETACH
        )
        else -> this
    }.doOnSubscribe {
        manageDisposables(it, uniqueId)
    }.doOnDispose {
        removeDisposable(uniqueId)
    }.doOnTerminate {
        removeDisposable(uniqueId)
    }.allowEmpty()
}

fun <T> Single<T>.untilLifecycleEnd(view: View, uniqueId: String? = null): Single<T> {
    return bindToLifecycle(view)
        .doOnSubscribe {
            manageDisposables(it, uniqueId)
        }.doOnDispose {
            removeDisposable(uniqueId)
        }.doOnTerminate {
            removeDisposable(uniqueId)
        }.allowEmpty()
}

fun <T> Single<T>.allowEmpty(): Single<T> {
    return onErrorResumeNext {
        when (it is CancellationException) {
            true -> Single.never()
            false -> Single.error<T>(it)
        }
    }
}

private val disposables = mutableMapOf<String, Disposable>()

@PublishedApi
internal fun manageDisposables(disposable: Disposable, uniqueId: String?) {
    uniqueId?.let {
        disposables[it]?.dispose()
        disposables.put(it, disposable)
    }
}

@PublishedApi
internal fun removeDisposable(uniqueId: String?) {
    uniqueId?.let { disposables.remove(it)?.dispose() }
}