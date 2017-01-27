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

package com.patloew.countries.ui.base.viewmodel;

import android.databinding.BaseObservable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.patloew.countries.ui.base.MvvmViewNotAttachedException;
import com.patloew.countries.ui.base.view.MvvmView;

import org.parceler.Parcels;

import javax.inject.Inject;

/**
 * Base class that implements the ViewModel interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvvmView that
 * can be accessed from the children classes by calling getMvpView().
 *
 * When saving state is required, restoring is handled automatically when calling attachView().
 * However, saveInstanceState() must still be called in the corresponding lifecycle callback.
 *
 * Create an inner class in your view model implementations that keeps the view state, so that
 * it can be injected here. Also, state saving/restoring is automatically handled by this base class.
 */
public abstract class BaseStateViewModel<T extends MvvmView, S> extends BaseObservable implements MvvmViewModel<T> {

    private final String KEY_STATE = "state";

    private T mvvmView;
    @Inject protected S state;

    @Override
    @CallSuper
    public void attachView(T mvpView, @Nullable Bundle savedInstanceState) {
        mvvmView = mvpView;
        if(savedInstanceState != null) { restoreInstanceState(savedInstanceState); }
    }

    @Override
    @CallSuper
    public void detachView() {
        mvvmView = null;
    }

    @Override
    public void saveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_STATE, Parcels.wrap(state));
    }

    protected void restoreInstanceState(@NonNull Bundle savedInstanceState) {
        if(savedInstanceState.containsKey(KEY_STATE)) {
            state = Parcels.unwrap(savedInstanceState.getParcelable(KEY_STATE));
        }
    }

    public final boolean isViewAttached() {
        return mvvmView != null;
    }

    public final T getMvvmView() {
        return mvvmView;
    }

    public final void checkViewAttached() {
        if (!isViewAttached()) throw new MvvmViewNotAttachedException();
    }

    public S getState() {
        return state;
    }
}

