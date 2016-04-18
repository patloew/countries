package com.patloew.countries;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.patloew.countries.dagger.CountryViewHolderModule;
import com.patloew.countries.dagger.DaggerCountryViewHolderComponent;
import com.patloew.countries.databinding.ActivityMainBinding;
import com.patloew.countries.databinding.CardCountryBinding;
import com.patloew.countries.model.Country;
import com.patloew.countries.view.CountryView;
import com.patloew.countries.view.MainActivityView;
import com.patloew.countries.viewmodel.CountryViewModel;
import com.patloew.countries.viewmodel.MainActivityViewModel;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import org.parceler.Parcels;

import java.util.ArrayList;
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
public class MainActivity extends AppCompatActivity implements MainActivityView {

    ActivityMainBinding binding;
    MainActivityViewModel viewModel;

    private List<Country> countryList = new ArrayList<>();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("countryList", Parcels.wrap(countryList));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new MainActivityViewModel(this, this);

        binding.rvCountries.setHasFixedSize(true);
        binding.rvCountries.setLayoutManager(new LinearLayoutManager(this));
        binding.rvCountries.setAdapter(new CountryAdapter());

        binding.srlCountries.setOnRefreshListener(() -> viewModel.onRefresh(false));

        if(savedInstanceState != null) {
            countryList = Parcels.unwrap(savedInstanceState.getParcelable("countryList"));
        } else {
            binding.srlCountries.post(() -> binding.srlCountries.setRefreshing(true));
            viewModel.onRefresh(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
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
            countryList = countries;
            binding.rvCountries.getAdapter().notifyDataSetChanged();
        } else {
            Snackbar.make(binding.rvCountries, "Could not load countries", Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.snackbar_action_retry, v -> viewModel.onRefresh(false))
                    .show();
        }

        binding.srlCountries.setRefreshing(false);
    }


    // Inner Classes for RecyclerView

    private class CountryAdapter extends RecyclerView.Adapter<CountryViewHolder> implements FastScrollRecyclerView.SectionedAdapter {

        @Override
        public CountryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.card_country, viewGroup, false);

            return new CountryViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CountryViewHolder countryViewHolder, int position) {
            countryViewHolder.viewModel.update(countryList, position);
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

    public class CountryViewHolder extends RecyclerView.ViewHolder implements CountryView {

        @Inject
        CountryViewModel viewModel;

        @Inject
        CardCountryBinding binding;

        public CountryViewHolder(View v) {
            super(v);

            DaggerCountryViewHolderComponent.builder()
                    .appComponent(CountriesApp.getAppComponent())
                    .countryViewHolderModule(new CountryViewHolderModule(this, v, MainActivity.this.viewModel.getRealm()))
                    .build()
                    .inject(this);

            binding.ivBookmark.setOnClickListener(bookmarkIcon -> viewModel.onBookmarkClick());
            binding.ivMap.setOnClickListener(mapImage -> viewModel.onMapClick());
        }

        @Override
        public void setIsBookmarked(boolean isBookmarked) {
            binding.ivBookmark.setImageResource(isBookmarked ? R.drawable.ic_bookmark_black : R.drawable.ic_bookmark_border_black);
        }
    }

}
