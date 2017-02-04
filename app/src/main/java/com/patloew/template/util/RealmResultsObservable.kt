package com.patloew.template.util

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.realm.RealmChangeListener
import io.realm.RealmObject
import io.realm.RealmResults

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
class RealmResultsObservable<T : RealmObject> private constructor(private val realmResults: RealmResults<T>) : ObservableOnSubscribe<RealmResults<T>> {

    @Throws(Exception::class)
    override fun subscribe(emitter: ObservableEmitter<RealmResults<T>>) {
        val changeListener = RealmChangeListener<RealmResults<T>> { emitter.onNext(it) }
        realmResults.addChangeListener(changeListener)
        emitter.setCancellable { realmResults.removeChangeListener(changeListener) }
    }

    companion object {

        fun <T : RealmObject> from(realmResults: RealmResults<T>): Observable<RealmResults<T>> {
            return Observable.create(RealmResultsObservable(realmResults))
        }
    }
}
