package com.ekoapp.rxlifecycle.extension.java;

import android.view.View;

import com.ekoapp.rxlifecycle.extension.MaybeKt;
import com.trello.rxlifecycle3.LifecycleProvider;

import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.MaybeTransformer;
import io.reactivex.annotations.NonNull;

public class MaybeExtension {

    public static <E, T> MaybeTransformer<T, T> untilLifecycleEnd(final LifecycleProvider<E> lifecycleProvider) {
        return untilLifecycleEnd(lifecycleProvider, null);
    }

    public static <E, T> MaybeTransformer<T, T> untilLifecycleEnd(final LifecycleProvider<E> lifecycleProvider, final String uniqueId) {
        return new MaybeTransformer<T, T>() {
            @NonNull
            @Override
            public MaybeSource<T> apply(@NonNull Maybe<T> upstream) {
                return MaybeKt.untilLifecycleEnd(upstream, lifecycleProvider, uniqueId);
            }
        };
    }

    public static <T> MaybeTransformer<T, T> untilLifecycleEnd(final View view) {
        return untilLifecycleEnd(view, null);
    }

    public static <T> MaybeTransformer<T, T> untilLifecycleEnd(final View view, final String uniqueId) {
        return new MaybeTransformer<T, T>() {
            @NonNull
            @Override
            public MaybeSource<T> apply(@NonNull Maybe<T> upstream) {
                return MaybeKt.untilLifecycleEnd(upstream, view, uniqueId);
            }
        };
    }
}