/* Copyright 2017 Tailored Media GmbH
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

package com.patloew.countries.util

import android.databinding.BaseObservable
import android.support.v7.widget.RecyclerView
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 *  Delegated property that calls notifyChange() every time the property get's changed.
 */
class NotifyChangeDelegate<T>(private var value: T) : ReadWriteProperty<BaseObservable,T>{
    override fun getValue(thisRef: BaseObservable, property: KProperty<*>): T {
        return value
    }

    override fun setValue(thisRef: BaseObservable, property: KProperty<*>, value: T) {
        if (this.value !== value) {
            this.value = value
            thisRef.notifyChange()
        }
    }
}

/**
 *  Delegated property that calls notifyPropertyChanged(propertyId) every time the property get's changed.
 */
class NotifyPropertyChangedDelegate<T>(private var value: T, private val propertyId: Int) : ReadWriteProperty<BaseObservable, T> {
    override fun getValue(thisRef: BaseObservable, property: KProperty<*>): T = value

    override fun setValue(thisRef: BaseObservable, property: KProperty<*>, value: T) {
        if (this.value !== value) {
            this.value = value
            thisRef.notifyPropertyChanged(propertyId)
        }
    }
}

/**
 *  Delegated property that calls notifyDataSetChanged() every time the property get's changed.
 */
class NotifyDatasetChangedDelegate<T>(private var value: T) : ReadWriteProperty<RecyclerView.Adapter<*>, T> {
    override fun getValue(thisRef: RecyclerView.Adapter<*>, property: KProperty<*>): T =  value

    override fun setValue(thisRef: RecyclerView.Adapter<*>, property: KProperty<*>, value: T) {
        if (this.value !== value) {
            this.value = value
            thisRef.notifyDataSetChanged()
        }
    }
}
