package com.patloew.countries.util

import android.databinding.ObservableField
import android.os.Parcel
import paperparcel.TypeAdapter

/* Copyright 2017 Patrick Löwenstein
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
class ObservableFieldPaperParcelTypeConverter<T>(val itemAdapter : TypeAdapter<T>) : TypeAdapter<ObservableField<T>> {

    override fun readFromParcel(source: Parcel): ObservableField<T> {
        return ObservableField<T>(itemAdapter.readFromParcel(source))
    }

    override fun writeToParcel(value: ObservableField<T>, dest: Parcel, flags: Int) {
        itemAdapter.writeToParcel(value.get(), dest, flags)
    }

}
