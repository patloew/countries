package com.tailoredapps.template.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.StringRes;

import com.tailoredapps.template.injection.qualifier.ActivityContext;
import com.tailoredapps.template.injection.scopes.PerActivity;

import javax.inject.Inject;

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
public class ProgressManager {

    private ProgressDialog progressDialog;

    private final Context ctx;

    @Inject
    public ProgressManager(@ActivityContext Context ctx) {
        this.ctx = ctx;
    }

    public void showProgress(@StringRes int titleRes, @StringRes int messageRes) {
        showProgress(titleRes, messageRes, true);
    }

    public void showProgress(@StringRes int titleRes, @StringRes int messageRes, boolean indeterminate) {
        hideProgress();
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setTitle(titleRes);
        progressDialog.setMessage(ctx.getString(messageRes));
        progressDialog.setIndeterminate(indeterminate);

        if(!indeterminate) {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(0);
        }

        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void setProgress(int progress) {
        if(progressDialog != null) {
            progressDialog.setProgress(progress);
        }
    }

    public void hideProgress() {
        if(progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
