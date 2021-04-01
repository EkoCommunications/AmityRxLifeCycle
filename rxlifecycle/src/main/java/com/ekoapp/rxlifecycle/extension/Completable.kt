package com.ekoapp.rxlifecycle.extension

import android.view.View
import com.trello.rxlifecycle3.LifecycleProvider
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.android.FragmentEvent
import com.trello.rxlifecycle3.kotlin.bindToLifecycle
import com.trello.rxlifecycle3.kotlin.bindUntilEvent
import io.reactivex.Completable
import java.util.concurrent.CancellationException

@Suppress("UNCHECKED_CAST")
inline fun <reified E> Completable.untilLifecycleEnd(lifecycleProvider: LifecycleProvider<E>, uniqueId: String? = null): Completable {
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