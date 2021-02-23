package com.ekoapp.rxlifecycle.extension.java;

import android.view.View;

import com.ekoapp.rxlifecycle.extension.FlowableKt;
import com.trello.rxlifecycle3.LifecycleProvider;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.annotations.NonNull;

public class FlowableExtension {

    public static <E, T> FlowableTransformer<T, T> untilLifecycleEnd(final LifecycleProvider<E> lifecycleProvider) {
        return untilLifecycleEnd(lifecycleProvider, null);
    }

    public static <E, T> FlowableTransformer<T, T> untilLifecycleEnd(final LifecycleProvider<E> lifecycleProvider, final String uniqueId) {
        return new FlowableTransformer<T, T>() {
            @NonNull
            @Override
            public Publisher<T> apply(@NonNull Flowable<T> upstream) {
                return FlowableKt.untilLifecycleEnd(upstream, lifecycleProvider, uniqueId);
            }
        };
    }

    public static <T> FlowableTransformer<T, T> untilLifecycleEnd(final View view) {
        return untilLifecycleEnd(view, null);
    }

    public static <T> FlowableTransformer<T, T> untilLifecycleEnd(final View view, final String uniqueId) {
        return new FlowableTransformer<T, T>() {
            @NonNull
            @Override
            public Publisher<T> apply(@NonNull Flowable<T> upstream) {
                return FlowableKt.untilLifecycleEnd(upstream, view, uniqueId);
            }
        };
    }
}