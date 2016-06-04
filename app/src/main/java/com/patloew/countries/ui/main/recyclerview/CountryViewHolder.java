package com.patloew.countries.ui.main.recyclerview;

import android.view.View;

import com.patloew.countries.databinding.CardCountryBinding;
import com.patloew.countries.ui.base.BaseViewHolder;
import com.patloew.countries.ui.base.MvvmView;

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
public class CountryViewHolder extends BaseViewHolder<CardCountryBinding, CountryViewModel> implements MvvmView {

    public CountryViewHolder(View v) {
        super(v);

        viewHolderComponent().inject(this);
        bindContentView(v);
    }
}
