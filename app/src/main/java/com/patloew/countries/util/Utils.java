package com.patloew.countries.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.Nullable;

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
public class Utils {

    @Nullable
    public static <T> T castActivityFromContext(Context context) {
        try {
            return (T) context;

        } catch(ClassCastException e1) {
            while(context instanceof ContextWrapper) {
                context = ((ContextWrapper)context).getBaseContext();
                try { return (T) context; } catch(ClassCastException e2) { }
            }

            return null;
        }
    }
}
