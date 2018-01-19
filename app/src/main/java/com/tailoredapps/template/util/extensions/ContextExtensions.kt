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

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import java.util.*


fun Context.getCurrentLocale(): Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    this.resources.configuration.locales.get(0)
} else {
    @Suppress("DEPRECATION")
    this.resources.configuration.locale
}

inline fun <reified T> Context.castWithUnwrap(): T? {
    if (this is T) return this

    var context = this
    while (context is ContextWrapper) {
        context = context.baseContext
        if (context is T) {
            return context
        }
    }
    return null
}
