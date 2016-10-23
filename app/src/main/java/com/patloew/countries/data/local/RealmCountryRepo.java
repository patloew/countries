package com.patloew.countries.data.local;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;

import com.patloew.countries.data.model.Country;
import com.patloew.countries.injection.scopes.PerApplication;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;

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
@PerApplication
@SuppressLint("NewApi") // try-with-resources is backported by retrolambda
public class RealmCountryRepo implements CountryRepo {

    private final Provider<Realm> realmProvider;

    @Inject
    public RealmCountryRepo(Provider<Realm> realmProvider) {
        this.realmProvider = realmProvider;
    }

    @Override
    public List<Country> findAllSorted(String sortField, Sort sort, boolean detached) {
        try(Realm realm = realmProvider.get()) {
            RealmResults<Country> realmResults = realm.where(Country.class).findAllSorted(sortField, sort);

            if(detached) {
                return realm.copyFromRealm(realmResults);
            } else {
                return realmResults;
            }
        }
    }

    @Override
    public Observable<List<Country>> findAllSortedWithChanges(String sortField, Sort sort) {
        try(Realm realm = realmProvider.get()) {
            return realm.where(Country.class).findAllSortedAsync(sortField, sort)
                    .asObservable()
                    .filter(RealmResults::isLoaded)
                    .map(result -> result);
        }
    }

    @Override
    @Nullable
    public Country getByField(String field, String value, boolean detached) {
        try(Realm realm = realmProvider.get()) {
            Country realmCountry = realm.where(Country.class).equalTo(field, value).findFirst();
            if(detached && realmCountry != null) { realmCountry = realm.copyFromRealm(realmCountry); }
            return realmCountry;
        }
    }

    @Override
    public void update(Country country) {
        try(Realm realm = realmProvider.get()) {
            realm.executeTransaction(r -> r.copyToRealmOrUpdate(country));
        }
    }

    @Override
    public void delete(Country realmCountry) {
        if(realmCountry.isValid()) {
            try (Realm realm = realmProvider.get()) {
                realm.executeTransaction(r -> {
                    realmCountry.borders.deleteAllFromRealm();
                    realmCountry.currencies.deleteAllFromRealm();
                    realmCountry.languages.deleteAllFromRealm();
                    realmCountry.translations.deleteAllFromRealm();
                    realmCountry.deleteFromRealm();
                });
            }
        }
    }

    @Override
    public Country detach(Country country) {
        if(country.isValid()) {
            try(Realm realm = realmProvider.get()) {
                return realm.copyFromRealm(country);
            }
        } else {
            return country;
        }
    }
}
