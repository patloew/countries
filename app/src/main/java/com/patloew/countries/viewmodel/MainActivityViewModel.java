package com.patloew.countries.viewmodel;

import android.content.Context;
import android.util.Log;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.patloew.countries.CountriesApp;
import com.patloew.countries.R;
import com.patloew.countries.model.Country;
import com.patloew.countries.network.ICountryApi;
import com.patloew.countries.view.MainActivityView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
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
public class MainActivityViewModel implements ActivityViewModel {

    private final Context ctx;
    private final MainActivityView mainActivityView;
    private final CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Inject
    Realm realm;

    @Inject
    ICountryApi countryApi;

    public MainActivityViewModel(Context activityContext, MainActivityView mainActivityView) {
        this.ctx = activityContext;
        this.mainActivityView = mainActivityView;
        CountriesApp.getAppComponent().inject(this);
    }

    public void onLicensesClick() {
        new LibsBuilder()
                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                .withActivityTitle(ctx.getString(R.string.menu_item_licenses))
                .withLibraries("rxJavaAndroid", "parceler", "recyclerview_fastscroll", "gradle_retrolambda")
                .withLicenseShown(true)
                .start(ctx);
    }

    public void onRefresh(boolean initialLoading) {
        if(initialLoading) {
            mainActivityView.onRefresh(true, realm.copyFromRealm(realm.where(Country.class).findAllSorted("name", Sort.ASCENDING)));
        }

        compositeSubscription.add(countryApi.getAllCountries()
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
                .subscribe(countries -> mainActivityView.onRefresh(true, countries),
                    throwable ->  {
                        Log.e("MainActivity", "Could not load countries", throwable);
                        mainActivityView.onRefresh(false, null);
                    }));
    }

    // Getter for sharing a single Realm instance for the Activity
    public Realm getRealm() {
        return realm;
    }

    @Override
    public void onDestroy() {
        realm.close();
        compositeSubscription.clear();
    }
}
