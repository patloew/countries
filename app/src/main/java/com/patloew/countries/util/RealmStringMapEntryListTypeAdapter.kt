package com.patloew.countries.util

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.patloew.countries.data.model.RealmStringMapEntry
import io.realm.RealmList
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
class RealmStringMapEntryListTypeAdapter private constructor() : TypeAdapter<RealmList<RealmStringMapEntry>>() {

    companion object {
        val INSTANCE = RealmStringMapEntryListTypeAdapter().nullSafe()
    }

    @Throws(IOException::class)
    override fun write(writer: JsonWriter, src: RealmList<RealmStringMapEntry>) {
        writer.beginObject()

        for (mapEntry in src) {
            writer.name(mapEntry.key)
            writer.value(mapEntry.value)
        }

        writer.endObject()
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): RealmList<RealmStringMapEntry> {
        val mapEntries = RealmList<RealmStringMapEntry>()

        reader.beginObject()

        while (reader.hasNext()) {
            val realmStringMapEntry = RealmStringMapEntry()
            realmStringMapEntry.key = reader.nextName()

            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull()
                realmStringMapEntry.value = null
            } else {
                realmStringMapEntry.value = reader.nextString()
            }

            mapEntries.add(realmStringMapEntry)
        }

        reader.endObject()

        return mapEntries
    }
}
