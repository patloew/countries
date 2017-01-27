/**
 * Copyright 2016 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 *
 * ----------
 *
 * FILE CHANGED 2017 Tailored Media GmbH
 */

package com.patloew.countries.util;

import io.reactivex.functions.Consumer;

public interface PlainConsumer<T> extends Consumer<T> {
    /**
     * Consume the given value.
     * @param t the value
     */
    @Override
    void accept(T t);
}
