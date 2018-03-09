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

package com.patloew.countries.ui

import android.graphics.drawable.Drawable
import com.patloew.countries.data.model.Country
import com.patloew.countries.ui.base.view.MvvmView
import com.patloew.countries.ui.base.viewmodel.MvvmViewModel

interface ICountryViewModel<V : MvvmView> : MvvmViewModel<V> {

    fun onMapClick()
    fun onBookmarkClick()
    fun update(country: Country, isLast: Boolean)

    // Properties

    val country: Country?
    val name: String
    val region: CharSequence
    val isCapitalVisible: Boolean
    val capital: CharSequence
    val population: CharSequence
    val isLocationVisible: Boolean
    val location: CharSequence
    val bookmarkDrawable: Drawable
    val isMapVisible: Boolean
    val cardBottomMargin: Int

}
