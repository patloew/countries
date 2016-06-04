package com.patloew.countries.ui.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.patloew.countries.BR;
import com.patloew.countries.CountriesApp;
import com.patloew.countries.injection.components.DaggerViewHolderComponent;
import com.patloew.countries.injection.components.ViewHolderComponent;

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
public abstract class BaseViewHolder<B extends ViewDataBinding, V extends ViewModel> extends RecyclerView.ViewHolder {

    protected B binding;
    @Inject protected V viewModel;

    private ViewHolderComponent viewHolderComponent;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    protected ViewHolderComponent viewHolderComponent() {
        if(viewHolderComponent == null) {
            viewHolderComponent = DaggerViewHolderComponent.builder()
                    .appComponent(CountriesApp.getAppComponent())
                    .build();
        }

        return viewHolderComponent;
    }

    protected void bindContentView(@NonNull View view) {
        if(viewModel == null) { throw new IllegalStateException("viewModel must not be null and should be injected via viewHolderComponent().inject(this)"); }
        binding = DataBindingUtil.bind(view);
        binding.setVariable(BR.vm, viewModel);
        //noinspection unchecked
        viewModel.attachView((MvvmView) this, null);
    }

    public V viewModel() { return viewModel; }
}
