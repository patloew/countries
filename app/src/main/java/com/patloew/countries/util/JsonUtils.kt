package com.patloew.countries.util

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken

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
object JsonUtils {
    @Throws(IOException::class)
    fun readNullSafeString(reader: JsonReader): String? {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
        } else {
            return reader.nextString()
        }
    }

    @Throws(IOException::class)
    fun readNullSafeLong(reader: JsonReader): Long? {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
        } else {
            return reader.nextLong()
        }
    }

    @Throws(IOException::class)
    fun readNullSafeInteger(reader: JsonReader): Int? {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
        } else {
            return reader.nextInt()
        }
    }

    @Throws(IOException::class)
    fun readNullSafeDouble(reader: JsonReader): Double? {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
        } else {
            return reader.nextDouble()
        }
    }

    @Throws(IOException::class)
    fun readNullSafeFloat(reader: JsonReader): Float? {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
        } else {
            return reader.nextDouble().toFloat()
        }
    }

    @Throws(IOException::class)
    fun readNullSafeBoolean(reader: JsonReader): Boolean? {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
        } else {
            return reader.nextBoolean()
        }
    }
}
