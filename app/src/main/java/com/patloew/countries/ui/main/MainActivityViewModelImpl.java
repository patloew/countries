package com.patloew.countries.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.patloew.countries.data.local.CountryRepo;
import com.patloew.countries.data.model.Country;
import com.patloew.countries.data.remote.CountryApi;
import com.patloew.countries.injection.scopes.PerActivity;
import com.patloew.countries.ui.base.BaseViewModel;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.realm.Sort;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

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
@PerActivity
public class MainActivityViewModelImpl extends BaseViewModel<MainActivityView> implements MainActivityViewModel {

    private final CompositeSubscription compositeSubscription = new CompositeSubscription();
    private final CountryRepo countryRepo;
    private final CountryApi countryApi;

    private List<Country> countryList = new ArrayList<>();

    @Inject
    public MainActivityViewModelImpl(CountryRepo countryRepo, CountryApi countryApi) {
        this.countryRepo = countryRepo;
        this.countryApi = countryApi;
    }

    @Override
    public void saveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable("countryList", Parcels.wrap(countryList));
    }

    @Override
    public void restoreInstanceState(@NonNull Bundle savedInstanceState) {
        countryList = Parcels.unwrap(savedInstanceState.getParcelable("countryList"));
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeSubscription.clear();
    }

    @Override
    public void onRefresh(boolean initialLoading) {
        if(initialLoading) {
            getView().onRefresh(true, countryRepo.findAllSorted("name", Sort.ASCENDING, true));
        }

        compositeSubscription.add(countryApi.getAllCountries()
                .doOnNext(Collections::sort)
                .map(countryRepo::update)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(countries -> getView().onRefresh(true, countries),
                    throwable ->  {
                        Log.e("MainActivity", "Could not load countries", throwable);
                        getView().onRefresh(false, null);
                    }));
    }
}
