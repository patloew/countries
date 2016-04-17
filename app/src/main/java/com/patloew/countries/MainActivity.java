package com.patloew.countries;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.patloew.countries.dagger.CountryViewHolderModule;
import com.patloew.countries.dagger.DaggerCountryViewHolderComponent;
import com.patloew.countries.databinding.ActivityMainBinding;
import com.patloew.countries.model.Country;
import com.patloew.countries.network.ICountryApi;
import com.patloew.countries.viewmodel.CountryViewModel;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.Sort;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

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
public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Inject
    Realm realm;

    @Inject
    ICountryApi countryApi;

    ActivityMainBinding binding;

    private List<Country> countryList = new ArrayList<>();

    private Subscription getCountriesSubscription;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("countryList", Parcels.wrap(countryList));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        CountriesApp.getAppComponent().inject(this);

        binding.rvCountries.setHasFixedSize(true);
        binding.rvCountries.setLayoutManager(new LinearLayoutManager(this));
        binding.rvCountries.setAdapter(new CountryAdapter());

        binding.srlCountries.setOnRefreshListener(this);

        if(savedInstanceState != null) {
            countryList = Parcels.unwrap(savedInstanceState.getParcelable("countryList"));
        } else {
            countryList = realm.copyFromRealm(realm.where(Country.class).findAllSorted("name", Sort.ASCENDING));
            binding.srlCountries.post(() -> binding.srlCountries.setRefreshing(true));
            onRefresh();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        if(getCountriesSubscription != null && !getCountriesSubscription.isUnsubscribed()) { getCountriesSubscription.unsubscribe(); }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_item_licenses) {
            new LibsBuilder()
                    .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                    .withActivityTitle(getString(R.string.menu_item_licenses))
                    .withLibraries("rxJavaAndroid", "parceler", "recyclerview_fastscroll", "gradle_retrolambda")
                    .withLicenseShown(true)
                    .start(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        if(getCountriesSubscription != null && !getCountriesSubscription.isUnsubscribed()) { getCountriesSubscription.unsubscribe(); }

        getCountriesSubscription = countryApi.getAllCountries()
                .doOnNext(Collections::sort)
                .map(sortedList -> {
                    // we need to open another Realm, because this happens
                    // on a background threead
                    try(Realm tempRealm = CountriesApp.getRealm()) {
                        List<Country> newCountryList = new ArrayList<>();

                        tempRealm.beginTransaction();
                        for(Country country : sortedList) {
                            if(tempRealm.where(Country.class).equalTo("alpha2Code", country.alpha2Code).findFirst() != null) {
                                // realm objects are live objects, the RealmObjects
                                // in the list are therefore updated
                                tempRealm.copyToRealmOrUpdate(country);
                            } else{
                                newCountryList.add(country);
                            }
                        }
                        tempRealm.commitTransaction();

                        for(Country country : tempRealm.where(Country.class).findAllSorted("name", Sort.DESCENDING)) {
                            newCountryList.add(0, tempRealm.copyFromRealm(country));
                        }

                        return newCountryList;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(countries -> {
                            countryList = countries;
                            binding.rvCountries.getAdapter().notifyDataSetChanged();
                        },
                        throwable -> {
                            binding.srlCountries.setRefreshing(false);
                            Log.e("MainActivity", "Could not load countries", throwable);
                            Snackbar.make(binding.rvCountries, "Could not load countries", Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.snackbar_action_retry, view -> onRefresh())
                                .show();
                        },
                        () -> binding.srlCountries.setRefreshing(false));
    }

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
            countryViewHolder.viewModel.update(countryList.get(position), countryList, position);
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

    public class CountryViewHolder extends RecyclerView.ViewHolder {

        @Inject
        CountryViewModel viewModel;

        public CountryViewHolder(View v) {
            super(v);

            DaggerCountryViewHolderComponent.builder()
                    .appComponent(CountriesApp.getAppComponent())
                    .countryViewHolderModule(new CountryViewHolderModule(v, realm))
                    .build()
                    .inject(this);
        }
    }

}
