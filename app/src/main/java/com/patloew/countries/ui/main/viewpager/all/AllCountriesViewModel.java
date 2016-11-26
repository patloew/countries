package com.patloew.countries.ui.main.viewpager.all;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.patloew.countries.data.local.CountryRepo;
import com.patloew.countries.data.remote.CountryApi;
import com.patloew.countries.injection.scopes.PerFragment;
import com.patloew.countries.ui.base.viewmodel.BaseViewModel;
import com.patloew.countries.ui.main.recyclerview.CountryAdapter;
import com.patloew.countries.ui.main.viewpager.CountriesView;

import java.util.Collections;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

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
public class AllCountriesViewModel extends BaseViewModel<CountriesView> implements IAllCountriesViewModel {

    private final CompositeSubscription compositeSubscription = new CompositeSubscription();

    private final CountryAdapter adapter;
    private final CountryApi countryApi;
    private final CountryRepo countryRepo;

    @Inject
    public AllCountriesViewModel(CountryAdapter adapter, CountryApi countryApi, CountryRepo countryRepo) {
        this.adapter = adapter;
        this.countryApi = countryApi;
        this.countryRepo = countryRepo;
    }

    @Override
    public void attachView(@NonNull CountriesView view, @Nullable Bundle savedInstanceState) {
        super.attachView(view, savedInstanceState);

        compositeSubscription.add(
                countryRepo.getFavoriteChangeObservable()
                    .subscribe(alpha2Code -> adapter.notifyDataSetChanged(), Timber::e)
        );
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeSubscription.clear();
    }

    @Override
    public void reloadData() {
        compositeSubscription.add(countryApi.getAllCountries()
                .doOnSuccess(Collections::sort)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(countries -> {
                    adapter.setCountryList(countries);
                    adapter.notifyDataSetChanged();
                    getView().onRefresh(true);
                }, throwable ->  {
                    Timber.e(throwable, "Could not load countries");
                    getView().onRefresh(false);
                }));
    }

    @Override
    public CountryAdapter getAdapter() {
        return adapter;
    }
}
