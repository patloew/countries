package com.patloew.countries.ui.main.recyclerview

import android.content.Context
import com.patloew.countries.data.local.CountryRepo
import com.patloew.countries.injection.qualifier.AppContext
import com.patloew.countries.injection.scopes.PerViewHolder
import com.patloew.countries.ui.BaseCountryViewModel
import com.patloew.countries.ui.base.navigator.Navigator
import com.patloew.countries.ui.base.view.MvvmView
import com.patloew.countries.ui.detail.DetailActivity
import javax.inject.Inject

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
 * FILE MODIFIED 2017 Tailored Media GmbH */

@PerViewHolder
class CountryViewModel
@Inject
constructor(@AppContext context: Context, navigator: Navigator, countryRepo: CountryRepo) : BaseCountryViewModel<MvvmView>(context, countryRepo, navigator), CountryMvvm.ViewModel {

    override fun onCardClick() {
        navigator.startActivity(DetailActivity::class.java) { putExtra(Navigator.EXTRA_ARG, countryRepo.detach(country)) }
    }

}
