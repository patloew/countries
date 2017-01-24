package com.tailoredapps.template.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.tailoredapps.template.data.model.RealmString;

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
public class RealmStringListTypeAdapter extends TypeAdapter<RealmList<RealmString>> {

    public static final TypeAdapter<RealmList<RealmString>> INSTANCE = new RealmStringListTypeAdapter().nullSafe();

    private RealmStringListTypeAdapter() { }

    @Override
    public void write(JsonWriter out, RealmList<RealmString> src) throws IOException {
        out.beginArray();

        for (RealmString realmString : src) {
            out.value(realmString.value);
        }

        out.endArray();
    }

    @Override
    public RealmList<RealmString> read(JsonReader in) throws IOException {
        RealmList<RealmString> realmStrings = new RealmList<>();

        in.beginArray();

        while (in.hasNext()) {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
            } else {
                RealmString realmString = new RealmString();
                realmString.value = in.nextString();
                realmStrings.add(realmString);
            }
        }

        in.endArray();

        return realmStrings;
    }
}
