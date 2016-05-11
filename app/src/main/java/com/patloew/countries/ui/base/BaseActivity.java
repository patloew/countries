package com.patloew.countries.ui.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import com.patloew.countries.BR;
import com.patloew.countries.CountriesApp;
import com.patloew.countries.injection.components.ActivityComponent;
import com.patloew.countries.injection.components.DaggerActivityComponent;
import com.patloew.countries.injection.modules.ActivityModule;

import javax.inject.Inject;

import io.realm.Realm;

/* Copyright 2016 Patrick Löwenstein
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
public class BaseActivity<B extends ViewDataBinding, V extends ViewModel> extends AppCompatActivity {

    protected B binding;
    @Inject protected V viewModel;

    // Always open a Realm in an Activity for avoiding open/close
    // overhead (a Realm instance is cached for each thread)
    @Inject Realm realm;

    private ActivityComponent mActivityComponent;

    public void setAndBindContentView(@LayoutRes int layoutResId) {
        if(viewModel == null) { throw new IllegalStateException("viewModel must not be null and should be injected via getActivityComponent().inject(this)"); }
        binding = DataBindingUtil.setContentView(this, layoutResId);
        binding.setVariable(BR.vm, viewModel);
    }

    public ActivityComponent getActivityComponent() {
        if(mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .appComponent(CountriesApp.getAppComponent())
                    .activityModule(new ActivityModule(this))
                    .build();
        }

        return mActivityComponent;
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        super.onDestroy();
        if(viewModel != null) { viewModel.detachView(); }
        if(realm != null) { realm.close(); }
        binding = null;
        viewModel = null;
        mActivityComponent = null;
        realm = null;

    }
}
