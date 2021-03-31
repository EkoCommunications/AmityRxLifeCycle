package com.ekoapp.rxlifecycle.extension

import android.view.View
import com.trello.rxlifecycle3.LifecycleProvider
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.android.FragmentEvent
import com.trello.rxlifecycle3.kotlin.bindToLifecycle
import com.trello.rxlifecycle3.kotlin.bindUntilEvent
import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import java.util.concurrent.CancellationException

private val disposables = mutableMapOf<String, Disposable>()

fun <E> Completable.untilLifecycleEnd(lifecycleProvider: LifecycleProvider<E>, uniqueId: String? = null): Completable {
    return ((lifecycleProvider as? LifecycleProvider<ActivityEvent>)?.let {
        bindUntilEvent(it, ActivityEvent.DESTROY)
    } ?: (lifecycleProvider as? LifecycleProvider<FragmentEvent>)?.let {
        bindUntilEvent(it, FragmentEvent.DESTROY)
    } ?: (lifecycleProvider as? LifecycleProvider<ViewEvent>)?.let {
        bindUntilEvent(it, ViewEvent.DETACH)
    } ?: this)
        .doOnSubscribe {
            manageDisposables(it, uniqueId)
        }.doOnDispose {
            removeDisposable(uniqueId)
        }.doOnTerminate {
            removeDisposable(uniqueId)
        }.allowInComplete()
}

fun Completable.untilLifecycleEnd(view: View, uniqueId: String? = null): Completable {
    return bindToLifecycle(view)
        .doOnSubscribe {
            manageDisposables(it, uniqueId)
        }.doOnDispose {
            removeDisposable(uniqueId)
        }.doOnTerminate {
            removeDisposable(uniqueId)
        }.allowInComplete()
}

fun Completable.allowInComplete(): Completable {
    return onErrorComplete {
        it is CancellationException
    }
}

private fun manageDisposables(subscription: Disposable, uniqueId: String?) {
    uniqueId?.let {
        disposables[it]?.dispose()
        disposables.put(it, subscription)
    }
}

private fun removeDisposable(uniqueId: String?) {
    uniqueId?.let { disposables.remove(it)?.dispose() }
}