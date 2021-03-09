package com.ekoapp.rxlifecycle.extension.java;

import android.view.View;

import com.ekoapp.rxlifecycle.extension.SingleKt;
import com.trello.rxlifecycle3.LifecycleProvider;

import io.reactivex.SingleTransformer;

public class SingleExtension {

    public static <E, T> SingleTransformer<T, T> untilLifecycleEnd(LifecycleProvider<E> lifecycleProvider) {
        return untilLifecycleEnd(lifecycleProvider, null);
    }

    public static <E, T> SingleTransformer<T, T> untilLifecycleEnd(LifecycleProvider<E> lifecycleProvider, final String uniqueId) {
        return upstream -> SingleKt.untilLifecycleEnd(upstream, lifecycleProvider, uniqueId);
    }

    public static <T> SingleTransformer<T, T> untilLifecycleEnd(View view) {
        return untilLifecycleEnd(view, null);
    }

    public static <T> SingleTransformer<T, T> untilLifecycleEnd(View view, String uniqueId) {
        return upstream -> SingleKt.untilLifecycleEnd(upstream, view, uniqueId);
    }
}