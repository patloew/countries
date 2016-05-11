package com.patloew.countries.ui.base;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patloew.countries.BR;
import com.patloew.countries.CountriesApp;
import com.patloew.countries.injection.components.DaggerFragmentComponent;
import com.patloew.countries.injection.components.FragmentComponent;
import com.patloew.countries.injection.modules.FragmentModule;

import java.lang.reflect.ParameterizedType;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public View setAndBindContentView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @LayoutRes int layoutResId) {
        if(viewModel == null) { throw new IllegalStateException("viewModel must not be null and should be injected via getFragmentComponent().inject(this)"); }
        View view = inflater.inflate(layoutResId, container, false);
        binding = getBinding(view);
        binding.setVariable(BR.vm, viewModel);
        return view;
    }

    private B getBinding(View view) {
        Class clazz = getClass();
        while(clazz.getSuperclass() != BaseFragment.class) { clazz = clazz.getSuperclass(); }

        try {
            //noinspection unchecked
            return (B) ((Class)(((ParameterizedType)clazz.getGenericSuperclass()).getActualTypeArguments()[0]))
                        .getMethod("bind", View.class)
                        .invoke(null, view);
        } catch (Exception e) {
            throw new IllegalStateException("Could not get binding for " + getClass().getSimpleName(), e);
        }
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        super.onDestroyView();
        if(viewModel != null) { viewModel.detachView(); }
        binding = null;
        viewModel = null;
    }

    @Override
    @CallSuper
    public void onDestroy() {
        mFragmentComponent = null;
        super.onDestroy();
    }
}
