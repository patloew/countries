package com.tailoredapps.template.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.tailoredapps.template.data.model.RealmStringMapEntry;

import java.io.IOException;

import io.realm.RealmList;

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
public class RealmStringMapEntryListTypeAdapter extends TypeAdapter<RealmList<RealmStringMapEntry>> {

    public static final TypeAdapter<RealmList<RealmStringMapEntry>> INSTANCE = new RealmStringMapEntryListTypeAdapter().nullSafe();

    private RealmStringMapEntryListTypeAdapter() { }

    @Override
    public void write(JsonWriter out, RealmList<RealmStringMapEntry> src) throws IOException {
        out.beginObject();

        for (RealmStringMapEntry mapEntry : src) {
            out.name(mapEntry.key);
            out.value(mapEntry.value);
        }

        out.endObject();
    }

    @Override
    public RealmList<RealmStringMapEntry> read(JsonReader in) throws IOException {
        RealmList<RealmStringMapEntry> mapEntries = new RealmList<>();

        in.beginObject();

        while (in.hasNext()) {
            RealmStringMapEntry realmStringMapEntry = new RealmStringMapEntry();
            realmStringMapEntry.key = in.nextName();

            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                realmStringMapEntry.value = null;
            } else {
                realmStringMapEntry.value = in.nextString();
            }

            mapEntries.add(realmStringMapEntry);
        }

        in.endObject();

        return mapEntries;
    }
}
