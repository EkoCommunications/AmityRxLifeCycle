package com.ekoapp.rxlifecycle.extension.java;

import android.view.View;

import com.ekoapp.rxlifecycle.extension.ObservableKt;
import com.trello.rxlifecycle3.LifecycleProvider;

import rx.Observable;

public class ObservableExtension {

    public static <E, T> Observable.Transformer<T, T> untilLifecycleEnd(LifecycleProvider<E> lifecycleProvider) {
        return untilLifecycleEnd(lifecycleProvider, null);
    }

    public static <E, T> Observable.Transformer<T, T> untilLifecycleEnd(LifecycleProvider<E> lifecycleProvider, String uniqueId) {
        return upstream -> ObservableKt.untilLifecycleEnd(upstream, lifecycleProvider, uniqueId);
    }

    public static <T> Observable.Transformer<T, T> untilLifecycleEnd(View view) {
        return untilLifecycleEnd(view, null);
    }

    public static <T> Observable.Transformer<T, T> untilLifecycleEnd(View view, String uniqueId) {
        return upstream -> ObservableKt.untilLifecycleEnd(upstream, view, uniqueId);
    }
}