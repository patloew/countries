package com.patloew.countries.data.local

import com.patloew.countries.data.model.Country
import com.patloew.countries.injection.scopes.PerApplication
import com.patloew.countries.util.RealmResultsObservable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.realm.Realm
import io.realm.Sort
import javax.inject.Inject
import javax.inject.Provider

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
class RealmCountryRepo
@Inject
constructor(private val realmProvider: Provider<Realm>) : CountryRepo {

    private val favoriteChangeSubject = PublishSubject.create<String>()
    override val favoriteChangeObservable: Observable<String>
        get() = favoriteChangeSubject

    override fun findAllSorted(sortField: String?, sort: Sort, detached: Boolean): List<Country> {
        realmProvider.get().use { realm ->
            val realmResults = realm.where(Country::class.java).findAllSorted(sortField, sort)

            if (detached) {
                return realm.copyFromRealm(realmResults)
            } else {
                return realmResults
            }
        }
    }

    override fun findAllSortedWithChanges(sortField: String?, sort: Sort): Observable<List<Country>> {
        realmProvider.get().use { realm ->
            return RealmResultsObservable.from(realm.where(Country::class.java).findAllSortedAsync(sortField, sort))
                    .filter({ it.isLoaded })
                    .map({ it })
        }
    }

    override fun getByField(field: String?, value: String?, detached: Boolean): Country? {
        realmProvider.get().use { realm ->
            var realmCountry: Country? = realm.where(Country::class.java).equalTo(field, value).findFirst()
            if (detached && realmCountry != null) {
                realmCountry = realm.copyFromRealm<Country>(realmCountry)
            }
            return realmCountry
        }
    }

    override fun save(country: Country) {
        realmProvider.get().use { realm ->
            realm.executeTransaction { r -> r.copyToRealmOrUpdate(country) }
            favoriteChangeSubject.onNext(country.alpha2Code)
        }
    }

    override fun delete(realmCountry: Country) {
        if (realmCountry.isValid) {
            realmProvider.get().use { realm ->
                val alpha2Code = realmCountry.alpha2Code

                realm.executeTransaction { r ->
                    realmCountry.borders?.deleteAllFromRealm()
                    realmCountry.currencies?.deleteAllFromRealm()
                    realmCountry.languages?.deleteAllFromRealm()
                    realmCountry.translations?.deleteAllFromRealm()
                    realmCountry.deleteFromRealm()
                }

                favoriteChangeSubject.onNext(alpha2Code)
            }
        }
    }

    override fun detach(country: Country): Country {
        if (country.isManaged) {
            realmProvider.get().use { realm -> return realm.copyFromRealm(country) }
        } else {
            return country
        }
    }
}
