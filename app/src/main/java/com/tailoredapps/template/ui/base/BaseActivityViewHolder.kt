package com.tailoredapps.template.ui.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tailoredapps.template.BR
import com.tailoredapps.template.injection.components.ActivityViewHolderComponent
import com.tailoredapps.template.injection.components.DaggerActivityViewHolderComponent
import com.tailoredapps.template.ui.base.view.MvvmView
import com.tailoredapps.template.ui.base.viewmodel.MvvmViewModel
import com.tailoredapps.template.util.extensions.attachViewOrThrowRuntimeException
import com.tailoredapps.template.util.extensions.castWithUnwrap
import timber.log.Timber
import javax.inject.Inject

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

/* Base class for ViewHolders when using a view model in an Activity with data binding.
 * This class provides the binding and the view model to the subclass. The
 * view model is injected and the binding is created when the content view is bound.
 * Each subclass therefore has to call the following code in the constructor:
 *    bindContentView(view)
 *
 * After calling this method, the binding and the view model is initialized.
 * saveInstanceState() and restoreInstanceState() are not called/used for ViewHolder
 * view models.
 *
 * Your subclass must implement the MvvmView implementation that you use in your
 * view model. */
abstract class BaseActivityViewHolder<B : ViewDataBinding, VM : MvvmViewModel<*>>(itemView: View) : RecyclerView.ViewHolder(itemView), MvvmView {

    protected lateinit var binding: B
    @Inject lateinit var viewModel: VM
        protected set

    protected val viewHolderComponent: ActivityViewHolderComponent by lazy {
        DaggerActivityViewHolderComponent.builder()
                .activityComponent(itemView.context.castWithUnwrap<BaseActivity<*, *>>()?.activityComponent)
                .build()
    }

    protected fun bindContentView(view: View) {
        try {
            ActivityViewHolderComponent::class.java.getDeclaredMethod("inject", this::class.java).invoke(viewHolderComponent, this)
        } catch(e: NoSuchMethodException) {
            throw RtfmException("You forgot to add \"fun inject(viewHolder: ${this::class.java.simpleName})\" in ActivityViewHolderComponent")
        }

        binding = DataBindingUtil.bind(view)
        binding.setVariable(BR.vm, viewModel)
        viewModel.attachViewOrThrowRuntimeException(this, null)
    }

    fun executePendingBindings() {
        binding.executePendingBindings()
    }

}
