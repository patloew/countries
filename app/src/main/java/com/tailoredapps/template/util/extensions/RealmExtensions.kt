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

package com.tailoredapps.template.util.extensions

import io.realm.Realm
import io.realm.RealmModel
import timber.log.Timber
import java.io.Closeable
import javax.inject.Provider
import kotlin.reflect.KMutableProperty1


inline fun <T : Closeable?, R> Provider<T>.use(consumer: (T) -> R): R = get().use(consumer)

fun <T> Realm.executeRealmTransactionWithResult(transaction: (Realm) -> T): T? {
    beginTransaction()
    return try {
        val result = transaction.invoke(this)
        commitTransaction()
        result
    } catch (t: Throwable) {
        if (isInTransaction) {
            cancelTransaction()
        } else {
            Timber.w(t, "Could not cancel transaction, not currently in a transaction.")
        }
        null
    }
}

inline fun <reified T : RealmModel> Realm.containsAtLeastOne(): Boolean = where(T::class.java).count() > 0

inline fun <reified M : RealmModel, T> Realm.getFieldFromFirstResult(mapper: (M) -> T): T? = where(M::class.java).findFirst()?.let(mapper)

inline fun <reified M : RealmModel, T> Realm.setFieldOnFirstResult(crossinline setter: M.(T) -> Unit, value: T) {
    where(M::class.java).findFirst()?.let { model -> executeTransaction { model.setter(value) } }
}

inline fun <reified M : RealmModel, T> Realm.setFieldOnFirstResult(setter: KMutableProperty1<M, T>, value: T) {
    where(M::class.java).findFirst()?.let { model -> executeTransaction { setter.set(model, value) } }
}

inline fun <reified M : RealmModel> Realm.findAll(detach: Boolean = false): List<M> {
    var list: List<M> = where(M::class.java).findAll()
    if (detach) {
        list = copyFromRealm(list)
    }
    return list
}

inline fun <reified M : RealmModel> Realm.findFirst(detach: Boolean = false): M? {
    var first: M? = where(M::class.java).findFirst()
    if (first != null && detach) {
        first = copyFromRealm(first)
    }
    return first
}