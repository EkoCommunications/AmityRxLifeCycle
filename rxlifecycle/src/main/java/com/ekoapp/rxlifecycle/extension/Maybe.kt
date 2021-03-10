package com.ekoapp.rxlifecycle.extension

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.ekoapp.rxlifecycle.ViewEvent
import com.trello.rxlifecycle3.LifecycleProvider
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.android.FragmentEvent
import com.trello.rxlifecycle3.kotlin.bindToLifecycle
import com.trello.rxlifecycle3.kotlin.bindUntilEvent
import io.reactivex.Maybe
import io.reactivex.disposables.Disposable

private val disposables = mutableMapOf<String, Disposable>()

fun <E, T> Maybe<T>.untilLifecycleEnd(lifecycleProvider: LifecycleProvider<E>, uniqueId: String? = null): Maybe<T> {
    return when (lifecycleProvider) {
        is Activity -> bindUntilEvent(
            lifecycleProvider as LifecycleProvider<ActivityEvent>,
            ActivityEvent.DESTROY
        )
        is Fragment -> bindUntilEvent(
            lifecycleProvider as LifecycleProvider<FragmentEvent>,
            FragmentEvent.DESTROY
        )
        is View -> bindUntilEvent(
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
    }
}

fun <T> Maybe<T>.untilLifecycleEnd(view: View, uniqueId: String? = null): Maybe<T> {
    return bindToLifecycle(view)
        .doOnSubscribe {
            manageDisposables(it, uniqueId)
        }.doOnDispose {
            removeDisposable(uniqueId)
        }.doOnTerminate {
            removeDisposable(uniqueId)
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