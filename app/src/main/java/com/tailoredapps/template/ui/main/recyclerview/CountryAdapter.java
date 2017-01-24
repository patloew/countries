package com.tailoredapps.template.ui.main.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import com.tailoredapps.template.R;
import com.tailoredapps.template.data.model.Country;
import com.tailoredapps.template.injection.scopes.PerFragment;

import java.util.Collections;
import java.util.List;

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
 * FILE MODIFIED 2017 Tailored Media GmbH */
@PerFragment
public class CountryAdapter extends RecyclerView.Adapter<CountryViewHolder> implements FastScrollRecyclerView.SectionedAdapter {

    private List<Country> countryList = Collections.emptyList();

    @Inject
    public CountryAdapter() { }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

    @Override
    public CountryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_country, viewGroup, false);

        return new CountryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CountryViewHolder countryViewHolder, int position) {
        countryViewHolder.viewModel().update(countryList.get(position), position == countryList.size()-1);
        countryViewHolder.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return countryList.get(position).name.substring(0, 1);
    }
}
