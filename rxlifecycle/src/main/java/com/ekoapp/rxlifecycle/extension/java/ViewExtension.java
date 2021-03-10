package com.ekoapp.rxlifecycle.extension.java;

import android.view.View;

import com.ekoapp.rxlifecycle.extension.ViewEvent;
import com.ekoapp.rxlifecycle.extension.ViewKt;
import com.trello.rxlifecycle3.LifecycleProvider;

public class ViewExtension {

    public static LifecycleProvider<ViewEvent> lifecycleProviderFromView(View view) {
        return ViewKt.lifecycleProviderFromView(view);
    }
}