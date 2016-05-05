package com.patloew.countries.data.model;

import com.patloew.countries.util.RealmListParcelConverter;

import org.parceler.Parcel;
import org.parceler.ParcelPropertyConverter;

import io.realm.CountryRealmProxy;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

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
@Parcel(implementations = { CountryRealmProxy.class },
        value = Parcel.Serialization.FIELD,
        analyze = { Country.class })
public class Country extends RealmObject implements Comparable<Country> {

    @PrimaryKey
    public String alpha2Code;
    public String alpha3Code;
    public String name;
    public String nativeName;
    public String region;
    public String capital;
    @ParcelPropertyConverter(RealmListParcelConverter.class)
    public RealmList<RealmString> currencies;
    @ParcelPropertyConverter(RealmListParcelConverter.class)
    public RealmList<RealmString> borders;
    @ParcelPropertyConverter(RealmListParcelConverter.class)
    public RealmList<RealmString> languages;
    @ParcelPropertyConverter(RealmListParcelConverter.class)
    public RealmList<RealmStringMapEntry> translations;
    public Integer population;
    public Float lat;
    public Float lng;

    @Override
    public int compareTo(Country another) {
        if(name != null && another.name != null) {
            return name.compareTo(another.name);
        } else {
            return 0;
        }
    }
}
