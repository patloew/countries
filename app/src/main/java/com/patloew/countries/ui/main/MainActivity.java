package com.patloew.countries.ui.main;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.patloew.countries.R;
import com.patloew.countries.data.model.Country;
import com.patloew.countries.databinding.ActivityMainBinding;
import com.patloew.countries.ui.base.BaseActivity;

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
public class MainActivity extends BaseActivity<ActivityMainBinding, MainActivityViewModel> implements MainActivityView {

    @Inject CountryAdapter adapter;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        viewModel.saveInstanceState(outState);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setAndBindContentView(R.layout.activity_main);
        viewModel.attachView(this, savedInstanceState);

        binding.rvCountries.setHasFixedSize(true);
        binding.rvCountries.setLayoutManager(new LinearLayoutManager(this));
        binding.rvCountries.setAdapter(adapter);

        binding.srlCountries.setOnRefreshListener(() -> viewModel.onRefresh(false));

        if(savedInstanceState == null) {
            binding.srlCountries.post(() -> binding.srlCountries.setRefreshing(true));
        }

        viewModel.onRefresh(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_item_licenses) {
            viewModel.onLicensesClick();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh(boolean success, List<Country> countries) {
        if(success) {
            adapter.setCountryList(countries);
            adapter.notifyDataSetChanged();
        } else {
            Snackbar.make(binding.rvCountries, "Could not load countries", Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.snackbar_action_retry, v -> viewModel.onRefresh(false))
                    .show();
        }

        binding.srlCountries.setRefreshing(false);
    }
}
