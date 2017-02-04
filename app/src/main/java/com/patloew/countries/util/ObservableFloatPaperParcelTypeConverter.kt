package com.patloew.countries.util

import android.databinding.ObservableFloat
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
class ObservableFloatPaperParcelTypeConverter() : TypeAdapter<ObservableFloat> {

    override fun readFromParcel(source: Parcel): ObservableFloat {
        return ObservableFloat(source.readFloat())
    }

    override fun writeToParcel(value: ObservableFloat, dest: Parcel, flags: Int) {
        dest.writeFloat(value.get())
    }

}
