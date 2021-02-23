package com.ekoapp.rxlifecycle.extension.java;

import android.view.View;

import com.ekoapp.rxlifecycle.extension.SingleKt;
import com.trello.rxlifecycle3.LifecycleProvider;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.annotations.NonNull;

public class SingleExtension {

    public static <E, T> SingleTransformer<T, T> untilLifecycleEnd(final LifecycleProvider<E> lifecycleProvider) {
        return untilLifecycleEnd(lifecycleProvider, null);
    }

    public static <E, T> SingleTransformer<T, T> untilLifecycleEnd(final LifecycleProvider<E> lifecycleProvider, final String uniqueId) {
        return new SingleTransformer<T, T>() {
            @NonNull
            @Override
            public SingleSource<T> apply(@NonNull Single<T> upstream) {
                return SingleKt.untilLifecycleEnd(upstream, lifecycleProvider, uniqueId);
            }
        };
    }

    public static <T> SingleTransformer<T, T> untilLifecycleEnd(final View view) {
        return untilLifecycleEnd(view, null);
    }

    public static <T> SingleTransformer<T, T> untilLifecycleEnd(final View view, final String uniqueId) {
        return new SingleTransformer<T, T>() {
            @NonNull
            @Override
            public SingleSource<T> apply(@NonNull Single<T> upstream) {
                return SingleKt.untilLifecycleEnd(upstream, view, uniqueId);
            }
        };
    }
}