package com.patloew.countries.data.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import paperparcel.PaperParcel
import paperparcel.PaperParcelable

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
@PaperParcel
open class Country : RealmObject(), Comparable<Country>, PaperParcelable {

    companion object {
        @JvmField val CREATOR = PaperParcelCountry.CREATOR
    }

    @PrimaryKey
    open var alpha2Code: String? = null
    open var alpha3Code: String? = null
    open var name: String? = null
    open var nativeName: String? = null
    open var region: String? = null
    open var capital: String? = null
    open var currencies: RealmList<RealmString>? = null
    open var borders: RealmList<RealmString>? = null
    open var languages: RealmList<RealmString>? = null
    open var translations: RealmList<RealmStringMapEntry>? = null
    open var population: Int? = null
    open var lat: Float? = null
    open var lng: Float? = null

    override fun compareTo(other: Country): Int {
        if (name != null && other.name != null) {
            return name!!.compareTo(other.name!!)
        } else {
            return 0
        }
    }
}
