package com.ekoapp.rxlifecycle.extension.java;

import android.util.Log;
import android.view.View;

import com.ekoapp.rxlifecycle.extension.SingleKt;
import com.ekoapp.rxlifecycle.extension.ViewEvent;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.trello.rxlifecycle3.kotlin.RxlifecycleKt;

import io.reactivex.Single;
import io.reactivex.SingleTransformer;

public class SingleExtension {

    public static <E, T> SingleTransformer<T, T> untilLifecycleEnd(LifecycleProvider<E> lifecycleProvider) {
        return untilLifecycleEnd(lifecycleProvider, null);
    }

    public static <E, T> SingleTransformer<T, T> untilLifecycleEnd(LifecycleProvider<E> lifecycleProvider, String uniqueId) {
        return upstream -> untilLifecycleEnd(lifecycleProvider, uniqueId, upstream)
                .doOnSubscribe(disposable -> SingleKt.manageDisposables(disposable, uniqueId))
                .doOnDispose(() -> SingleKt.removeDisposable(uniqueId))
                .doOnTerminate(() -> SingleKt.removeDisposable(uniqueId));
    }

    private static <E, T> Single<T> untilLifecycleEnd(LifecycleProvider<E> lifecycleProvider, String uniqueId, Single<T> upstream) {
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
                Log.d(SingleExtension.class.getName(), e.getMessage(), e);
            }
        }
    }

    public static <T> SingleTransformer<T, T> untilLifecycleEnd(View view) {
        return untilLifecycleEnd(view, null);
    }

    public static <T> SingleTransformer<T, T> untilLifecycleEnd(View view, String uniqueId) {
        return upstream -> SingleKt.untilLifecycleEnd(upstream, view, uniqueId);
    }
}