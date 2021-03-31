package com.ekoapp.rxlifecycle.extension

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.trello.rxlifecycle3.LifecycleProvider
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.android.FragmentEvent
import com.trello.rxlifecycle3.kotlin.bindToLifecycle
import com.trello.rxlifecycle3.kotlin.bindUntilEvent
import io.reactivex.Flowable
import org.reactivestreams.Subscription

private val subscriptions = mutableMapOf<String, Subscription>()

fun <E, T> Flowable<T>.untilLifecycleEnd(lifecycleProvider: LifecycleProvider<E>, uniqueId: String? = null): Flowable<T> {
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
    }.doOnCancel {
        removeSubscription(uniqueId)
    }.doOnTerminate {
        removeSubscription(uniqueId)
    }
}

fun <T> Flowable<T>.untilLifecycleEnd(view: View, uniqueId: String? = null): Flowable<T> {
    return bindToLifecycle(view)
        .doOnSubscribe {
            manageDisposables(it, uniqueId)
        }.doOnCancel {
            removeSubscription(uniqueId)
        }.doOnTerminate {
            removeSubscription(uniqueId)
        }
}

private fun manageDisposables(subscription: Subscription, uniqueId: String?) {
    uniqueId?.let {
        subscriptions[it]?.cancel()
        subscriptions.put(it, subscription)
    }
}

private fun removeSubscription(uniqueId: String?) {
    uniqueId?.let { subscriptions.remove(it)?.cancel() }
}