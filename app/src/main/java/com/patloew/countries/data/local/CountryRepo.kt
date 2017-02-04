package com.patloew.countries.data.local

import com.patloew.countries.data.model.Country

import io.reactivex.Observable
import io.realm.Sort

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
interface CountryRepo {
    val favoriteChangeObservable: Observable<String>

    fun findAllSorted(sortField: String?, sort: Sort, detached: Boolean): List<Country>
    fun findAllSortedWithChanges(sortField: String?, sort: Sort): Observable<List<Country>>

    fun getByField(field: String?, value: String?, detached: Boolean): Country?

    fun save(country: Country)
    fun delete(country: Country)

    fun detach(country: Country): Country
}
