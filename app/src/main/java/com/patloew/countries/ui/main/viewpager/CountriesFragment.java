package com.patloew.countries.ui.main.viewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patloew.countries.R;
import com.patloew.countries.data.model.Country;
import com.patloew.countries.databinding.FragmentRecyclerviewBinding;
import com.patloew.countries.ui.base.BaseFragment;
import com.patloew.countries.ui.base.viewmodel.MvvmViewModel;
import com.patloew.countries.ui.main.recyclerview.CountryAdapter;

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
 * limitations under the License. */
public abstract class CountriesFragment<V extends MvvmViewModel> extends BaseFragment<FragmentRecyclerviewBinding, V> implements CountriesMvvm.View {

    @Inject CountryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setAndBindContentView(inflater, container, R.layout.fragment_recyclerview, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    // View

    @Override
    public void onRefresh(boolean success, List<Country> countries) {
        if(success) {
            adapter.setCountryList(countries);
            adapter.notifyDataSetChanged();
        }

        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }
}
