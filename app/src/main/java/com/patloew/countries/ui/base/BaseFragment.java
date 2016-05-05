package com.patloew.countries.ui.base;

import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;

import com.patloew.countries.CountriesApp;
import com.patloew.countries.injection.components.DaggerFragmentComponent;
import com.patloew.countries.injection.components.FragmentComponent;
import com.patloew.countries.injection.modules.FragmentModule;

import javax.inject.Inject;

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
public class BaseFragment<B extends ViewDataBinding, V extends ViewModel> extends Fragment {

    protected B binding;
    @Inject protected V viewModel;

    private FragmentComponent mFragmentComponent;

    public FragmentComponent getFragmentComponent() {
        if(mFragmentComponent == null) {
            mFragmentComponent = DaggerFragmentComponent.builder()
                    .appComponent(CountriesApp.getAppComponent())
                    .fragmentModule(new FragmentModule(this))
                    .build();
        }

        return mFragmentComponent;
    }
}
