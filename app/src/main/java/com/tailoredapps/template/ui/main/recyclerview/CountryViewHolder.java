package com.tailoredapps.template.ui.main.recyclerview;

import android.view.View;

import com.tailoredapps.template.databinding.CardCountryBinding;
import com.tailoredapps.template.ui.base.BaseViewHolder;
import com.tailoredapps.template.ui.base.view.MvvmView;


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
 * FILE MODIFIED 2017 Tailored Media GmbH */
public class CountryViewHolder extends BaseViewHolder<CardCountryBinding, CountryMvvm.ViewModel> implements MvvmView {

    public CountryViewHolder(View v) {
        super(v);

        viewHolderComponent().inject(this);
        bindContentView(v);
    }
}
