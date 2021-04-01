package com.ekoapp.rxlifecycle.extension.java;

import android.util.Log;
import android.view.View;

import com.ekoapp.rxlifecycle.extension.FlowableKt;
import com.ekoapp.rxlifecycle.extension.ViewEvent;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.trello.rxlifecycle3.kotlin.RxlifecycleKt;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;

public class FlowableExtension {

    public static <E, T> FlowableTransformer<T, T> untilLifecycleEnd(LifecycleProvider<E> lifecycleProvider) {
        return untilLifecycleEnd(lifecycleProvider, null);
    }

    public static <E, T> FlowableTransformer<T, T> untilLifecycleEnd(LifecycleProvider<E> lifecycleProvider, String uniqueId) {
        return upstream -> untilLifecycleEnd(lifecycleProvider, uniqueId, upstream)
                .doOnSubscribe(disposable -> FlowableKt.manageSubscriptions(disposable, uniqueId))
                .doOnCancel(() -> FlowableKt.removeSubscription(uniqueId))
                .doOnTerminate(() -> FlowableKt.removeSubscription(uniqueId));
    }

    static <E, T> Flowable<T> untilLifecycleEnd(LifecycleProvider<E> lifecycleProvider, String uniqueId, Flowable<T> upstream) {
        int count = 0;
        while (true) {
            try {
                switch (count++) {
                    case 0:
                        return RxlifecycleKt.bindUntilEvent(upstream, (LifecycleProvider<ActivityEvent>) lifecycleProvider, ActivityEvent.DESTROY);
                    case 1:
                        return RxlifecycleKt.bindUntilEvent(upstream, (LifecycleProvider<FragmentEvent>) lifecycleProvider, FragmentEvent.DESTROY);
                    case 2:
                        return RxlifecycleKt.bindUntilEvent(upstream, (LifecycleProvider<ViewEvent>) lifecycleProvider, ViewEvent.DETACH);
                    case 3:
                        return upstream;
                }
            } catch (ClassCastException e) {
                Log.d(FlowableExtension.class.getName(), e.getMessage(), e);
            }
        }
    }

    public static <T> FlowableTransformer<T, T> untilLifecycleEnd(View view) {
        return untilLifecycleEnd(view, null);
    }

    public static <T> FlowableTransformer<T, T> untilLifecycleEnd(View view, String uniqueId) {
        return upstream -> FlowableKt.untilLifecycleEnd(upstream, view, uniqueId);
    }
}