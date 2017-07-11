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

package com.tailoredapps.template.util.kotlin

import android.databinding.BaseObservable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/*  Delegated property that calls notifyPropertyChanged() every time the property gets changed. */
class NotifyPropertyChangedDelegate<T>(var value: T, val propertyId: Int) : ReadWriteProperty<BaseObservable, T> {
    override inline fun getValue(thisRef: BaseObservable, property: KProperty<*>): T = value

    override inline fun setValue(thisRef: BaseObservable, property: KProperty<*>, value: T) {
        if(this.value != value) {
            this.value = value
            thisRef.notifyPropertyChanged(propertyId)
        }
    }
}