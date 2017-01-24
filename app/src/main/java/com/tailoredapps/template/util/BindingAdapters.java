package com.tailoredapps.template.util;

import android.databinding.BindingAdapter;
import android.view.View;

public class BindingAdapters {

    @BindingAdapter("android:visibility")
    public static void setVisibility(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
