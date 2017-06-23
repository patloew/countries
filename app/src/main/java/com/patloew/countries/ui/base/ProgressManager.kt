package com.patloew.countries.ui.base

import android.app.ProgressDialog
import android.content.Context
import android.support.annotation.StringRes

import com.patloew.countries.injection.qualifier.ActivityContext
import com.patloew.countries.injection.scopes.PerActivity

import javax.inject.Inject

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
 * limitations under the License. */

@PerActivity
class ProgressManager
@Inject
constructor(@ActivityContext private val ctx: Context) {

    private var progressDialog: ProgressDialog? = null

    fun showProgress(@StringRes titleRes: Int, @StringRes messageRes: Int, indeterminate: Boolean = true) {
        hideProgress()
        progressDialog = ProgressDialog(ctx)
        progressDialog!!.setTitle(titleRes)
        progressDialog!!.setMessage(ctx.getString(messageRes))
        progressDialog!!.isIndeterminate = indeterminate

        if (!indeterminate) {
            progressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            progressDialog!!.progress = 0
        }

        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
    }

    fun setProgress(progress: Int) {
        progressDialog?.progress = progress
    }

    fun hideProgress() {
        progressDialog?.dismiss()
        progressDialog = null
    }
}
