package com.patloew.countries.ui.main.recyclerview;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.patloew.countries.data.model.Country;
import com.patloew.countries.ui.base.view.MvvmView;
import com.patloew.countries.ui.base.viewmodel.MvvmViewModel;

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
public interface CountryMvvm {

    interface ViewModel<V extends MvvmView> extends MvvmViewModel<V> {

        void onMapClick(View view);
        void onBookmarkClick(View view);
        void update(Country country, boolean isLast);

        // Properties

        Country getCountry();
        String getName();
        CharSequence getRegion();
        int getCapitalVisibility();
        CharSequence getCapital();
        CharSequence getPopulation();
        int getLocationVisibility();
        CharSequence getLocation();
        Drawable getBookmarkDrawable();
        int getMapVisibility();
        int getCardBottomMargin();

    }
}
