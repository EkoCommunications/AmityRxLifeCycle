package com.ekoapp.rxlifecycle.extension.java;

import android.view.View;

import com.ekoapp.rxlifecycle.extension.CompletableKt;
import com.trello.rxlifecycle3.LifecycleProvider;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.CompletableTransformer;
import io.reactivex.annotations.NonNull;

public class CompletableExtension {

    public static <E> CompletableTransformer untilLifecycleEnd(final LifecycleProvider<E> lifecycleProvider) {
        return untilLifecycleEnd(lifecycleProvider, null);
    }

    public static <E> CompletableTransformer untilLifecycleEnd(final LifecycleProvider<E> lifecycleProvider, final String uniqueId) {
        return new CompletableTransformer() {
            @NonNull
            @Override
            public CompletableSource apply(@NonNull Completable upstream) {
                return CompletableKt.untilLifecycleEnd(upstream, lifecycleProvider, uniqueId);
            }
        };
    }

    public static CompletableTransformer untilLifecycleEnd(final View view) {
        return untilLifecycleEnd(view, null);
    }

    public static CompletableTransformer untilLifecycleEnd(final View view, final String uniqueId) {
        return new CompletableTransformer() {
            @NonNull
            @Override
            public CompletableSource apply(@NonNull Completable upstream) {
                return CompletableKt.untilLifecycleEnd(upstream, view, uniqueId);
            }
        };
    }
}