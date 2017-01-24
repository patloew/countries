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

package com.tailoredapps.template.ui;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.tailoredapps.template.data.model.Country;
import com.tailoredapps.template.ui.base.view.MvvmView;
import com.tailoredapps.template.ui.base.viewmodel.MvvmViewModel;

public interface ICountryViewModel<V extends MvvmView> extends MvvmViewModel<V> {

    void onMapClick(View view);
    void onBookmarkClick(View view);
    void update(Country country, boolean isLast);

    // Properties

    Country getCountry();
    String getName();
    CharSequence getRegion();
    boolean isCapitalVisible();
    CharSequence getCapital();
    CharSequence getPopulation();
    boolean isLocationVisible();
    CharSequence getLocation();
    Drawable getBookmarkDrawable();
    boolean isMapVisible();
    int getCardBottomMargin();

}
