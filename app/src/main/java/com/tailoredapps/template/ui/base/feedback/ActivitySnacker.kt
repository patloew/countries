/* Copyright 2017 Tailored Media GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.*/

package com.tailoredapps.template.ui.base.feedback

import android.support.annotation.StringRes
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentActivity
import android.view.ViewGroup

open class ActivitySnacker(val activity: FragmentActivity) : Snacker {

    private var actionSnackbar: Snackbar? = null
    private var snackbar: Snackbar? = null

    override fun show(title: CharSequence) = showInternal(title, null, null)
    override fun show(@StringRes titleRes: Int) = showInternal(activity.getString(titleRes), null, null)

    override fun show(title: CharSequence, actionText: CharSequence, action: () -> Unit) = showInternal(title, action, actionText)
    override fun show(titleRes: Int, actionTextRes: Int, action: () -> Unit) = showInternal(activity.getString(titleRes), action, activity.getString(actionTextRes))


    private fun showInternal(title: CharSequence, action: (() -> Unit)?, actionText: CharSequence?) {
        hideSnack()

        val coordinator = activity.findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        val attachView = if (coordinator != null && coordinator is CoordinatorLayout) coordinator else activity.findViewById<ViewGroup>(android.R.id.content)

        if (action != null && actionText != null) {
            actionSnackbar = Snackbar.make(attachView, title, Snackbar.LENGTH_INDEFINITE)
                    .setAction(actionText) { action() }
                    .apply { show() }
        } else {
            snackbar = Snackbar.make(attachView, title, Snackbar.LENGTH_LONG)
                    .apply { show() }
        }
    }

    override fun hideSnack() {
        actionSnackbar?.dismiss()
        actionSnackbar = null
        snackbar?.dismiss()
        snackbar = null
    }
}