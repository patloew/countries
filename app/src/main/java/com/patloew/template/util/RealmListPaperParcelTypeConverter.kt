package com.patloew.template.util

import android.os.Parcel
import io.realm.RealmList
import io.realm.RealmObject
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
class RealmListPaperParcelTypeConverter<T : RealmObject>(val itemAdapter : TypeAdapter<T>) : TypeAdapter<RealmList<T>> {

    override fun readFromParcel(source: Parcel): RealmList<T> {
        val size = source.readInt()
        val realmList = RealmList<T>()
        for (i in 0..size - 1) { realmList.add(itemAdapter.readFromParcel(source)) }
        return realmList
    }

    override fun writeToParcel(value: RealmList<T>, dest: Parcel, flags: Int) {
        dest.writeInt(value.size)
        for (i in 0..value.size - 1) { itemAdapter.writeToParcel(value[i], dest, flags) }
    }

}
