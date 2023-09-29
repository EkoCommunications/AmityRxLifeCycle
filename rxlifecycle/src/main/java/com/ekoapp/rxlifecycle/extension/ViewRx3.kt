package com.ekoapp.rxlifecycle.extension

import android.view.View
import com.trello.rxlifecycle4.LifecycleProvider
import com.trello.rxlifecycle4.LifecycleTransformer
import com.trello.rxlifecycle4.RxLifecycle
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

fun View.lifecycleProviderFromViewRx3(): LifecycleProvider<ViewEvent> {
    val subject = BehaviorSubject.create<ViewEvent>()

    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(view: View?) {
            subject.onNext(ViewEvent.ATTACH)
        }

        override fun onViewDetachedFromWindow(view: View) {
            subject.onNext(ViewEvent.DETACH)
        }
    })

    return object : LifecycleProvider<ViewEvent> {
        override fun lifecycle(): Observable<ViewEvent> {
            return subject.hide()
        }

        override fun <T : Any?> bindUntilEvent(event: ViewEvent): LifecycleTransformer<T> {
            return RxLifecycle.bindUntilEvent<T, ViewEvent>(lifecycle(), event)
        }

        override fun <T : Any?> bindToLifecycle(): LifecycleTransformer<T> {
            return bindUntilEvent(ViewEvent.DETACH)
        }
    }
}