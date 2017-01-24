package com.tailoredapps.template.ui.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tailoredapps.template.BR;
import com.tailoredapps.template.injection.components.DaggerViewHolderComponent;
import com.tailoredapps.template.injection.components.ViewHolderComponent;
import com.tailoredapps.template.ui.base.view.MvvmView;
import com.tailoredapps.template.ui.base.viewmodel.MvvmViewModel;
import com.tailoredapps.template.ui.base.viewmodel.NoOpViewModel;
import com.tailoredapps.template.util.Utils;

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
 * limitations under the License.
 *
 * ------
 *
 * FILE MODIFIED 2017 Tailored Media GmbH
 */

/* Base class for ViewHolders when using a view model with data binding.
 * This class provides the binding and the view model to the subclass. The
 * view model is injected and the binding is created when the content view is bound.
 * Each subclass therefore has to call the following code in the constructor:
 *    viewHolderComponent().inject(this);
 *    bindContentView(view);
 *
 * After calling these methods, the binding and the view model is initialized.
 * saveInstanceState() and restoreInstanceState() are not called/used for ViewHolder
 * view models.
 *
 * Your subclass must implement the MvvmView implementation that you use in your
 * view model. */
public abstract class BaseViewHolder<B extends ViewDataBinding, V extends MvvmViewModel> extends RecyclerView.ViewHolder {

    protected B binding;
    @Inject protected V viewModel;

    protected final View itemView;

    private ViewHolderComponent viewHolderComponent;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    protected final ViewHolderComponent viewHolderComponent() {
        if(viewHolderComponent == null) {
            viewHolderComponent = DaggerViewHolderComponent.builder()
                    .activityComponent(Utils.castActivityFromContext(itemView.getContext(), BaseActivity.class).activityComponent())
                    .build();
        }

        return viewHolderComponent;
    }

    protected final void bindContentView(@NonNull View view) {
        if(viewModel == null) { throw new IllegalStateException("viewModel must not be null and should be injected via viewHolderComponent().inject(this)"); }
        binding = DataBindingUtil.bind(view);
        binding.setVariable(BR.vm, viewModel);

        try {
            //noinspection unchecked
            viewModel.attachView((MvvmView) this, null);
        } catch(ClassCastException e) {
            if (!(viewModel instanceof NoOpViewModel)) {
                throw new RuntimeException(getClass().getSimpleName() + " must implement MvvmView subclass as declared in " + viewModel.getClass().getSimpleName());
            }
        }
    }

    public final V viewModel() {
        return viewModel;
    }

    public final void executePendingBindings() {
        if(binding != null) { binding.executePendingBindings(); }
    }
}
