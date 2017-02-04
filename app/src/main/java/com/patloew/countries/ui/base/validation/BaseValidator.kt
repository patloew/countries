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

package com.patloew.countries.ui.base.validation

import io.reactivex.Completable

/***
 * Base class for validators of a view state.

 * @param <S> The state class to be validated
</S> */
abstract class BaseValidator<in S> {

    /***
     * Validates the state.

     * @param state The state to be validated.
     * *
     * @return A Completable that completes when the state is valid and
     * *     provides a ValidationException in onError when the state is invalid.
     */
    fun validate(state: S): Completable {
        return Completable.fromAction { validateState(state) }
    }

    /***
     * Implement this method to validate the state. Here you can also
     * set error fields, e.g. for showing errors on an EditText.
     * If the state is invalid, throw a ValidationException.

     * @param state The state to be validated.
     * *
     * @throws ValidationException When the state is invalid.
     */
    @Throws(ValidationException::class)
    protected abstract fun validateState(state: S)
}
