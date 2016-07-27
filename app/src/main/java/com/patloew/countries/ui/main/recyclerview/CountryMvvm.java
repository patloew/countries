package com.patloew.countries.ui.main.recyclerview;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.patloew.countries.data.model.Country;
import com.patloew.countries.ui.base.MvvmView;
import com.patloew.countries.ui.base.MvvmViewModel;

import java.util.List;

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

    interface ViewModel extends MvvmViewModel<MvvmView> {

        void onMapClick(View view);
        void onBookmarkClick(View view);
        void update(List<Country> countryList, int layoutPosition);

        // Properties

        String getName();
        CharSequence getNameTranslations();
        CharSequence getRegion();
        int getCapitalVisibility();
        CharSequence getCapital();
        CharSequence getPopulation();
        CharSequence getLanguages();
        CharSequence getCurrencies();
        int getLocationVisibility();
        CharSequence getLocation();
        int getBorderVisibility();
        CharSequence getBorders();
        Drawable getBookmarkDrawable();
        int getMapVisibility();
        int getCardBottomMargin();

    }
}
