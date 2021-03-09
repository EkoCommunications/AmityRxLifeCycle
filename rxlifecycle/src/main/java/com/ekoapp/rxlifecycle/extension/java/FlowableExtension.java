package com.ekoapp.rxlifecycle.extension.java;

import android.view.View;

import com.ekoapp.rxlifecycle.extension.FlowableKt;
import com.trello.rxlifecycle3.LifecycleProvider;

import io.reactivex.FlowableTransformer;

public class FlowableExtension {

    public static <E, T> FlowableTransformer<T, T> untilLifecycleEnd(LifecycleProvider<E> lifecycleProvider) {
        return untilLifecycleEnd(lifecycleProvider, null);
    }

    public static <E, T> FlowableTransformer<T, T> untilLifecycleEnd(LifecycleProvider<E> lifecycleProvider, String uniqueId) {
        return upstream -> FlowableKt.untilLifecycleEnd(upstream, lifecycleProvider, uniqueId);
    }

    public static <T> FlowableTransformer<T, T> untilLifecycleEnd(View view) {
        return untilLifecycleEnd(view, null);
    }

    public static <T> FlowableTransformer<T, T> untilLifecycleEnd(View view, String uniqueId) {
        return upstream -> FlowableKt.untilLifecycleEnd(upstream, view, uniqueId);
    }
}