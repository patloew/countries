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
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentActivity
import com.tailoredapps.template.injection.scopes.PerActivity
import javax.inject.Inject

@PerActivity
open class ActivitySnacker
@Inject
constructor(override val activity: FragmentActivity, override val defaultActionText: CharSequence) : Snacker {

    private var actionSnackbar: Snackbar? = null
    private var snackbar: Snackbar? = null

    override fun show(title: CharSequence, action: (() -> Unit)?, actionText: CharSequence?) = showInternal(title, action, actionText)
    override fun show(title: CharSequence, action: (() -> Unit)?, @StringRes actionText: Int?) = showInternal(title, action, if (actionText != null) activity.getString(actionText) else null)

    override fun show(@StringRes titleRes: Int, action: (() -> Unit)?, @StringRes actionText: Int?) = showInternal(activity.getString(titleRes), action, if (actionText != null) activity.getString(actionText) else null)
    override fun show(@StringRes titleRes: Int, action: (() -> Unit)?, actionText: CharSequence?) = showInternal(activity.getString(titleRes), action, actionText)


    private fun showInternal(title: CharSequence, action: (() -> Unit)?, actionText: CharSequence?) {
        if (action != null) {
            hideActionSnack()
            actionSnackbar = Snackbar.make(activity.findViewById(android.R.id.content), title, Snackbar.LENGTH_INDEFINITE)
                    .setAction(actionText ?: defaultActionText) { action() }
            actionSnackbar?.show()
        } else {
            hideSnack()
            snackbar = Snackbar.make(activity.findViewById(android.R.id.content), title, Snackbar.LENGTH_LONG)
            snackbar?.show()
        }
    }

    override fun hideActionSnack() {
        actionSnackbar?.dismiss()
        actionSnackbar = null
    }

    override fun hideSnack() {
        snackbar?.dismiss()
        snackbar = null
    }
}