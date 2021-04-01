package com.ekoapp.rxlifecycle.extension.java;

import android.util.Log;
import android.view.View;

import com.ekoapp.rxlifecycle.extension.MaybeKt;
import com.ekoapp.rxlifecycle.extension.SingleKt;
import com.ekoapp.rxlifecycle.extension.ViewEvent;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.trello.rxlifecycle3.kotlin.RxlifecycleKt;

import io.reactivex.Maybe;
import io.reactivex.MaybeTransformer;

public class MaybeExtension {

    public static <E, T> MaybeTransformer<T, T> untilLifecycleEnd(LifecycleProvider<E> lifecycleProvider) {
        return untilLifecycleEnd(lifecycleProvider, null);
    }

    public static <E, T> MaybeTransformer<T, T> untilLifecycleEnd(LifecycleProvider<E> lifecycleProvider, String uniqueId) {
        return upstream -> untilLifecycleEnd(lifecycleProvider, uniqueId, upstream)
                .doOnSubscribe(disposable -> SingleKt.manageDisposables(disposable, uniqueId))
                .doOnDispose(() -> SingleKt.removeDisposable(uniqueId))
                .doOnTerminate(() -> SingleKt.removeDisposable(uniqueId));
    }

    private static <E, T> Maybe<T> untilLifecycleEnd(LifecycleProvider<E> lifecycleProvider, String uniqueId, Maybe<T> upstream) {
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
                Log.d(MaybeExtension.class.getName(), e.getMessage(), e);
            }
        }
    }

    public static <T> MaybeTransformer<T, T> untilLifecycleEnd(View view) {
        return untilLifecycleEnd(view, null);
    }

    public static <T> MaybeTransformer<T, T> untilLifecycleEnd(View view, String uniqueId) {
        return upstream -> MaybeKt.untilLifecycleEnd(upstream, view, uniqueId);
    }
}