package com.patloew.countries.ui.main.viewpager.all;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.patloew.countries.data.model.Country;
import com.patloew.countries.data.remote.CountryApi;
import com.patloew.countries.injection.scopes.PerFragment;
import com.patloew.countries.ui.base.BaseViewModel;
import com.patloew.countries.ui.main.viewpager.CountriesMvvm;
import com.patloew.countries.util.ParcelUtil;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
public class AllCountriesViewModel extends BaseViewModel<CountriesMvvm.View> implements IAllCountriesViewModel {

    private static final String KEY_COUNTRYLIST = "countryList";

    private final CompositeSubscription compositeSubscription = new CompositeSubscription();
    private final CountryApi countryApi;

    private List<Country> countryList = new ArrayList<>();

    @Inject
    public AllCountriesViewModel(CountryApi countryApi) {
        this.countryApi = countryApi;
    }

    @Override
    public void saveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(KEY_COUNTRYLIST, Parcels.wrap(countryList));
    }

    @Override
    public void restoreInstanceState(@NonNull Bundle savedInstanceState) {
        countryList = ParcelUtil.getParcelable(savedInstanceState, KEY_COUNTRYLIST, countryList);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeSubscription.clear();
    }

    @Override
    public void onRefresh(boolean initialLoading) {
        compositeSubscription.add(countryApi.getAllCountries()
                .doOnNext(Collections::sort)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(countries -> {
                    countryList = countries;
                    getView().onRefresh(true, countries);
                }, throwable ->  {
                    Timber.e(throwable, "Could not load countries");
                    getView().onRefresh(false, null);
                }));
    }
}
