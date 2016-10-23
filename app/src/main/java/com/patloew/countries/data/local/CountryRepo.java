package com.patloew.countries.data.local;

import android.support.annotation.Nullable;

import com.patloew.countries.data.model.Country;

import java.util.List;

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
public interface CountryRepo {
    Observable<String> getFavoriteChangeObservable();

    List<Country> findAllSorted(String sortField, Sort sort, boolean detached);
    Observable<List<Country>> findAllSortedWithChanges(String sortField, Sort sort);

    @Nullable
    Country getByField(String field, String value, boolean detached);

    void save(Country country);
    void delete(Country country);

    Country detach(Country country);
}
