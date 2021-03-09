package com.ekoapp.rxlifecycle.extension.java;

import android.view.View;

import com.ekoapp.rxlifecycle.extension.MaybeKt;
import com.trello.rxlifecycle3.LifecycleProvider;

import io.reactivex.MaybeTransformer;

public class MaybeExtension {

    public static <E, T> MaybeTransformer<T, T> untilLifecycleEnd(LifecycleProvider<E> lifecycleProvider) {
        return untilLifecycleEnd(lifecycleProvider, null);
    }

    public static <E, T> MaybeTransformer<T, T> untilLifecycleEnd(LifecycleProvider<E> lifecycleProvider, final String uniqueId) {
        return upstream -> MaybeKt.untilLifecycleEnd(upstream, lifecycleProvider, uniqueId);
    }

    public static <T> MaybeTransformer<T, T> untilLifecycleEnd(View view) {
        return untilLifecycleEnd(view, null);
    }

    public static <T> MaybeTransformer<T, T> untilLifecycleEnd(View view, String uniqueId) {
        return upstream -> MaybeKt.untilLifecycleEnd(upstream, view, uniqueId);
    }
}