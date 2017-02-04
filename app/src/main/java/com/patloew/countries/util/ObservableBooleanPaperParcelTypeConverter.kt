package com.patloew.countries.util

import android.databinding.ObservableBoolean
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
class ObservableBooleanPaperParcelTypeConverter() : TypeAdapter<ObservableBoolean> {

    override fun readFromParcel(source: Parcel): ObservableBoolean {
        return ObservableBoolean(source.readByte() == 1.toByte())
    }

    override fun writeToParcel(value: ObservableBoolean, dest: Parcel, flags: Int) {
        dest.writeByte(if(value.get()) 1.toByte() else 0.toByte())
    }

}
