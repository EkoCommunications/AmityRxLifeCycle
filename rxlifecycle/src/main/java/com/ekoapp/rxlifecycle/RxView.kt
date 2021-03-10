package com.ekoapp.rxlifecycle

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.trello.rxlifecycle3.LifecycleProvider
import com.trello.rxlifecycle3.LifecycleTransformer
import com.trello.rxlifecycle3.RxLifecycle
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

abstract class RxView : LinearLayout, LifecycleProvider<ViewEvent> {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val subject = BehaviorSubject.create<ViewEvent>()

    override fun lifecycle(): Observable<ViewEvent> {
        return subject.hide()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        subject.onNext(ViewEvent.ATTACH)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        subject.onNext(ViewEvent.DETACH)
    }

    override fun <T : Any?> bindUntilEvent(event: ViewEvent): LifecycleTransformer<T> {
        return RxLifecycle.bindUntilEvent<T, ViewEvent>(lifecycle(), event)
    }

    override fun <T : Any?> bindToLifecycle(): LifecycleTransformer<T> {
        return bindUntilEvent(ViewEvent.DETACH)
    }
}