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

package com.patloew.countries.ui.base.viewmodel

import android.databinding.BaseObservable
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.CallSuper

import com.patloew.countries.ui.base.MvvmViewNotAttachedException
import com.patloew.countries.ui.base.view.MvvmView
import com.patloew.countries.util.getParcelable

import javax.inject.Inject

/**
 * Base class that implements the ViewModel interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvvmView that
 * can be accessed from the children classes by calling getMvpView().

 * When saving state is required, restoring is handled automatically when calling attachView().
 * However, saveInstanceState() must still be called in the corresponding lifecycle callback.

 * Create an inner class in your view model implementations that keeps the view state, so that
 * it can be injected here. Also, state saving/restoring is automatically handled by this base class.
 */
abstract class BaseStateViewModel<V : MvvmView, S : Parcelable> : BaseObservable(), MvvmViewModel<V> {

    private val KEY_STATE = "state"

    var view: V? = null
        private set

    @Inject protected lateinit var state: S

    @CallSuper
    override fun attachView(view: V, savedInstanceState: Bundle?) {
        this.view = view
        if (savedInstanceState != null) { restoreInstanceState(savedInstanceState) }
    }

    @CallSuper
    override fun detachView() {
        view = null
    }

    override fun saveInstanceState(outState: Bundle?) {
        outState?.putParcelable(KEY_STATE, state)
    }

    protected fun restoreInstanceState(savedInstanceState: Bundle) {
        if (savedInstanceState.containsKey(KEY_STATE)) {
            state = savedInstanceState.getParcelable(KEY_STATE, state)
        }
    }

    val isViewAttached: Boolean
        get() = view != null

    fun checkViewAttached() {
        if (!isViewAttached) throw MvvmViewNotAttachedException()
    }
}

