package com.patloew.template.ui.base

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.*
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.patloew.template.BR
import com.patloew.template.injection.components.DaggerFragmentComponent
import com.patloew.template.injection.components.FragmentComponent
import com.patloew.template.injection.modules.FragmentModule
import com.patloew.template.injection.scopes.PerFragment
import com.patloew.template.ui.base.view.MvvmView
import com.patloew.template.ui.base.viewmodel.MvvmViewModel
import com.patloew.template.ui.base.viewmodel.NoOpViewModel
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
 *    if(viewModel == null) { getFragmentComponent().inject(this); }
 *    return setAndBindContentView(inflater, container, R.layout.my_fragment_layout, savedInstanceState);
 *
 * After calling these methods, the binding and the view model is initialized.
 * saveInstanceState() and restoreInstanceState() methods of the view model
 * are automatically called in the appropriate lifecycle events when above calls
 * are made.
 *
 * Your subclass must implement the MvvmView implementation that you use in your
 * view model. */
abstract class BaseFragment<B : ViewDataBinding, V : MvvmView, VM : MvvmViewModel<V>> : Fragment() {

    protected lateinit var binding: B
    @Inject protected lateinit var viewModel: VM
    @Inject protected lateinit var refWatcher: RefWatcher


    protected lateinit var fragmentComponent : FragmentComponent
        private set

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        viewModel.saveInstanceState(outState)
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.detachView()
        if(!viewModel.javaClass.isAnnotationPresent(PerFragment::class.java)) {
            refWatcher.watch(viewModel)
        }
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        refWatcher.watch(this)
        refWatcher.watch(fragmentComponent)
    }

    override fun onAttach(context: Context?) {
        fragmentComponent = DaggerFragmentComponent.builder()
                .fragmentModule(FragmentModule(this))
                .activityComponent((activity as BaseActivity<*, *, *>).activityComponent)
                .build()

        super.onAttach(context)
    }

    /* Sets the content view, creates the binding and attaches the view to the view model */
    protected fun setAndBindContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?, @LayoutRes layoutResID: Int): View {
        binding = DataBindingUtil.inflate<B>(inflater, layoutResID, container, false)
        binding.setVariable(BR.vm, viewModel)

        try {
            @Suppress("UNCHECKED_CAST")
            viewModel.attachView(this as V, savedInstanceState)
        } catch (e: ClassCastException) {
            if (viewModel !is NoOpViewModel<*>) {
                throw RuntimeException(javaClass.simpleName + " must implement MvvmView subclass as declared in " + viewModel.javaClass.simpleName)
            }
        }

        return binding.root
    }

    fun dimen(@DimenRes resId: Int): Int {
        return resources.getDimension(resId).toInt()
    }

    fun color(@ColorRes resId: Int): Int {
        return resources.getColor(resId)
    }

    fun integer(@IntegerRes resId: Int): Int {
        return resources.getInteger(resId)
    }

    fun string(@StringRes resId: Int): String {
        return resources.getString(resId)
    }
}
