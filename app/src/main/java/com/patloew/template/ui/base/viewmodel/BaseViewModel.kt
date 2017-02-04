package com.patloew.template.ui.base.viewmodel

import android.databinding.BaseObservable
import android.os.Bundle
import android.support.annotation.CallSuper

import com.patloew.template.ui.base.MvvmViewNotAttachedException
import com.patloew.template.ui.base.view.MvvmView

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
/**
 * Base class that implements the ViewModel interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvvmView that
 * can be accessed from the children classes by calling getMvpView().

 * When saving state is required, restoring is handled automatically when calling attachView().
 * However, saveInstanceState() must still be called in the corresponding lifecycle callback.

 * ------

 * FILE MODIFIED 2017 Tailored Media GmbH
 */
abstract class BaseViewModel<V : MvvmView> : BaseObservable(), MvvmViewModel<V> {

    var view: V? = null
        private set

    @CallSuper
    override fun attachView(view: V, savedInstanceState: Bundle?) {
        this.view = view
        if (savedInstanceState != null) { restoreInstanceState(savedInstanceState) }
    }

    @CallSuper
    override fun detachView() {
        view = null
    }

    protected open fun restoreInstanceState(savedInstanceState: Bundle) { }

    override fun saveInstanceState(outState: Bundle?) { }

    val isViewAttached: Boolean
        get() = view != null

    fun checkViewAttached() {
        if (!isViewAttached) throw MvvmViewNotAttachedException()
    }
}
