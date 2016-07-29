package com.patloew.countries.ui.main.viewpager.all;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.patloew.countries.R;
import com.patloew.countries.data.model.Country;
import com.patloew.countries.ui.main.viewpager.CountriesFragment;

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
public class AllCountriesFragment extends CountriesFragment<IAllCountriesViewModel> {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentComponent().inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.swipeRefreshLayout.setOnRefreshListener(() -> viewModel.onRefresh(false));

        if(savedInstanceState == null) {
            binding.swipeRefreshLayout.post(() -> binding.swipeRefreshLayout.setRefreshing(true));
        }

        viewModel.onRefresh(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        notifyDataSetChanged();
    }

    // View

    @Override
    public void onRefresh(boolean success, List<Country> countries) {
        super.onRefresh(success, countries);

        if(!success) {
            Snackbar.make(binding.recyclerView, "Could not load countries", Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.snackbar_action_retry, v -> viewModel.onRefresh(false))
                    .show();
        }
    }
}
