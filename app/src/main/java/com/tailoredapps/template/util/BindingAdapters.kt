package com.tailoredapps.template.util

import android.databinding.BindingAdapter
import android.view.View
import android.view.ViewGroup

object BindingAdapters {

    @BindingAdapter("android:visibility")
    @JvmStatic
    fun setVisibility(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    @BindingAdapter("android:layout_marginBottom")
    @JvmStatic
    fun setLayoutMarginBottom(v: View, bottomMargin: Int) {
        val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.bottomMargin = bottomMargin
    }
}
