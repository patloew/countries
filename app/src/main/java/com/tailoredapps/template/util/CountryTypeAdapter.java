package com.tailoredapps.template.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.tailoredapps.template.data.model.Country;

import java.io.IOException;

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
public class CountryTypeAdapter extends TypeAdapter<Country> {

    public static final TypeAdapter<Country> INSTANCE = new CountryTypeAdapter().nullSafe();

    private CountryTypeAdapter() { }

    @Override
    public void write(JsonWriter out, Country value) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Country read(JsonReader in) throws IOException {
        try {
            Country country = new Country();

            in.beginObject();

            while (in.hasNext()) {
                String name = in.nextName();

                switch (name) {
                    case "alpha2Code": {
                        // cannot be null, because it is the primary key
                        country.alpha2Code = in.nextString();
                        break;
                    }
                    case "alpha3Code": {
                        country.alpha3Code = JsonUtils.readNullSafeString(in);
                        break;
                    }
                    case "name": {
                        country.name = JsonUtils.readNullSafeString(in);
                        break;
                    }
                    case "nativeName": {
                        country.nativeName = JsonUtils.readNullSafeString(in);
                        break;
                    }
                    case "region": {
                        country.region = JsonUtils.readNullSafeString(in);
                        break;
                    }
                    case "capital": {
                        country.capital = JsonUtils.readNullSafeString(in);
                        break;
                    }
                    case "population": {
                        country.population = JsonUtils.readNullSafeInteger(in);
                        break;
                    }
                    case "latlng": {
                        if (in.peek() == JsonToken.NULL) {
                            in.nextNull();
                        } else {
                            in.beginArray();
                            if(in.hasNext()) { country.lat = JsonUtils.readNullSafeFloat(in); }
                            if(in.hasNext()) { country.lng = JsonUtils.readNullSafeFloat(in); }
                            in.endArray();
                        }
                        break;
                    }
                    case "currencies": {
                        country.currencies = RealmStringListTypeAdapter.INSTANCE.read(in);
                        break;
                    }
                    case "borders": {
                        country.borders = RealmStringListTypeAdapter.INSTANCE.read(in);
                        break;
                    }
                    case "languages": {
                        country.languages = RealmStringListTypeAdapter.INSTANCE.read(in);
                        break;
                    }
                    case "translations": {
                        country.translations = RealmStringMapEntryListTypeAdapter.INSTANCE.read(in);
                        break;
                    }
                    default:
                        in.skipValue();
                        break;
                }
            }

            in.endObject();

            return country;
        } catch(Exception e) {
            throw new IOException(e);
        }
    }
}
