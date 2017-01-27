package com.patloew.template.util;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.parceler.Parcels;

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
public class ParcelUtil {

    /* Gets a Parcelable wrapped with Parceler from a Bundle and returns null
     * if the bundle does not contain a value for key. */
    @Nullable
    public static <T> T getParcelable(@NonNull Bundle bundle, @NonNull String key) {
        //noinspection ConstantConditions
        return getParcelable(bundle, key, null);
    }

    /* Gets a Parcelable wrapped with Parceler from a Bundle and returns defaultObject
     * if the bundle does not contain a value for key. */
    @NonNull
    public static <T> T getParcelable(@NonNull Bundle bundle, @NonNull String key, @NonNull T defaultObject) {
        if(bundle.containsKey(key)) {
            return Parcels.unwrap(bundle.getParcelable(key));
        } else {
            return defaultObject;
        }
    }
}
