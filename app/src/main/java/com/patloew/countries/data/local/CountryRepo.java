package com.patloew.countries.data.local;

import android.support.annotation.Nullable;

import com.patloew.countries.data.model.Country;

import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

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
public interface CountryRepo {
    List<Country> findAllSorted(String sortField, Sort sort, boolean detached);
    RealmResults<Country> findAllSortedWithListener(String sortField, Sort sort, RealmChangeListener<RealmResults<Country>> listener);

    @Nullable
    Country getByField(String field, String value, boolean detached);

    void update(Country country);
    void delete(Country country);

    Country detach(Country country);
}
