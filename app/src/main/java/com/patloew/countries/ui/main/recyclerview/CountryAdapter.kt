package com.patloew.countries.ui.main.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.patloew.countries.R
import com.patloew.countries.data.model.Country
import com.patloew.countries.injection.scopes.PerFragment
import com.patloew.countries.util.Utils
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
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
 * limitations under the License. */
@PerFragment
class CountryAdapter
@Inject
constructor() : RecyclerView.Adapter<CountryViewHolder>(), FastScrollRecyclerView.SectionedAdapter {

    var countryList = emptyList<Country>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CountryViewHolder {
        return Utils.createViewHolder(viewGroup, R.layout.card_country, ::CountryViewHolder)
    }

    override fun onBindViewHolder(countryViewHolder: CountryViewHolder, position: Int) {
        countryViewHolder.viewModel.update(countryList[position], position == countryList.size - 1)
        countryViewHolder.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    override fun getSectionName(position: Int): String {
        return countryList[position].name!!.substring(0, 1)
    }
}
