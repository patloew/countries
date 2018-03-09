package com.patloew.countries.ui.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.patloew.countries.BR
import com.patloew.countries.injection.components.DaggerFragmentComponent
import com.patloew.countries.injection.components.FragmentComponent
import com.patloew.countries.injection.modules.FragmentModule
import com.patloew.countries.injection.scopes.PerFragment
import com.patloew.countries.ui.base.view.MvvmView
import com.patloew.countries.ui.base.viewmodel.MvvmViewModel
import com.patloew.countries.util.extensions.attachViewOrThrowRuntimeException
import com.squareup.leakcanary.RefWatcher
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
 * FILE MODIFIED 2017 Tailored Media GmbH */

/* Base class for Fragments when using a view model with data binding.
 * This class provides the binding and the view model to the subclass. The
 * view model is injected and the binding is created when the content view is set.
 * Each subclass therefore has to call the following code in onCreateView():
 *    return setAndBindContentView(inflater, container, savedInstanceState, R.layout.my_fragment_layout)
 *
 * After calling this method, the binding and the view model is initialized.
 * saveInstanceState() and restoreInstanceState() methods of the view model
 * are automatically called in the appropriate lifecycle events when above calls
 * are made.
 *
 * Your subclass must implement the MvvmView implementation that you use in your
 * view model. */
abstract class BaseFragment<B : ViewDataBinding, VM : MvvmViewModel<*>> : Fragment(), MvvmView {

    protected lateinit var binding: B
    @Inject protected lateinit var viewModel: VM
    @Inject protected lateinit var refWatcher: RefWatcher


    internal val fragmentComponent : FragmentComponent by lazy {
        DaggerFragmentComponent.builder()
                .fragmentModule(FragmentModule(this))
                .activityComponent((activity as BaseActivity<*, *>).activityComponent)
                .build()
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.saveInstanceState(outState)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            FragmentComponent::class.java.getDeclaredMethod("inject", this::class.java).invoke(fragmentComponent, this)
        } catch(e: NoSuchMethodException) {
            throw RtfmException("You forgot to add \"fun inject(fragment: ${this::class.java.simpleName})\" in FragmentComponent")
        }
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.detachView()
        if (!viewModel.javaClass.isAnnotationPresent(PerFragment::class.java)) {
            refWatcher.watch(viewModel)
        }
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        refWatcher.watch(this)
        refWatcher.watch(fragmentComponent)
    }


    /* Sets the content view, creates the binding and attaches the view to the view model */
    protected fun setAndBindContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?, @LayoutRes layoutResID: Int): View {
        binding = DataBindingUtil.inflate<B>(inflater, layoutResID, container, false)
        binding.setVariable(BR.vm, viewModel)
        viewModel.attachViewOrThrowRuntimeException(this, savedInstanceState)
        return binding.root
    }

}
