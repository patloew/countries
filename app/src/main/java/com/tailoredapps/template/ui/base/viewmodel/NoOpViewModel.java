package com.tailoredapps.template.ui.base.viewmodel;

import android.databinding.BaseObservable;
import android.os.Bundle;

import com.tailoredapps.template.ui.base.view.MvvmView;

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
 * -------
 *
 * FILE CHANGED 2017 Tailored Media GmbH
 * */
public final class NoOpViewModel extends BaseObservable implements MvvmViewModel<MvvmView> {

    @Inject
    public NoOpViewModel() { }

    @Override
    public void attachView(MvvmView mvvmView, Bundle savedInstanceState) { }

    @Override
    public void saveInstanceState(Bundle outState) { }

    @Override
    public void detachView() { }

}