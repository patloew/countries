package com.patloew.countries.util

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.patloew.countries.data.model.Country

import java.io.IOException

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
class CountryTypeAdapter private constructor() : TypeAdapter<Country>() {

    companion object {
        val INSTANCE = CountryTypeAdapter().nullSafe()
    }

    @Throws(IOException::class)
    override fun write(writer: JsonWriter, value: Country) {
        throw UnsupportedOperationException()
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): Country {
        try {
            val country = Country()

            reader.beginObject()

            while (reader.hasNext()) {
                val name = reader.nextName()

                when (name) {
                    "alpha2Code" -> {
                        // cannot be null, because it is the primary key
                        country.alpha2Code = reader.nextString()
                    }
                    "alpha3Code" -> {
                        country.alpha3Code = JsonUtils.readNullSafeString(reader)
                    }
                    "name" -> {
                        country.name = JsonUtils.readNullSafeString(reader)
                    }
                    "nativeName" -> {
                        country.nativeName = JsonUtils.readNullSafeString(reader)
                    }
                    "region" -> {
                        country.region = JsonUtils.readNullSafeString(reader)
                    }
                    "capital" -> {
                        country.capital = JsonUtils.readNullSafeString(reader)
                    }
                    "population" -> {
                        country.population = JsonUtils.readNullSafeInteger(reader)
                    }
                    "latlng" -> {
                        if (reader.peek() == JsonToken.NULL) {
                            reader.nextNull()
                        } else {
                            reader.beginArray()
                            if (reader.hasNext()) {
                                country.lat = JsonUtils.readNullSafeFloat(reader)
                            }
                            if (reader.hasNext()) {
                                country.lng = JsonUtils.readNullSafeFloat(reader)
                            }
                            reader.endArray()
                        }
                    }
                    "currencies" -> {
                        country.currencies = RealmStringListTypeAdapter.INSTANCE.read(reader)
                    }
                    "borders" -> {
                        country.borders = RealmStringListTypeAdapter.INSTANCE.read(reader)
                    }
                    "languages" -> {
                        country.languages = RealmStringListTypeAdapter.INSTANCE.read(reader)
                    }
                    "translations" -> {
                        country.translations = RealmStringMapEntryListTypeAdapter.INSTANCE.read(reader)
                    }
                    else -> reader.skipValue()
                }
            }

            reader.endObject()

            return country
        } catch (e: Exception) {
            throw IOException(e)
        }

    }
}
