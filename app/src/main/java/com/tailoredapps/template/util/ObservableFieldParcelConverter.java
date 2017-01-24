package com.tailoredapps.template.util;

import android.databinding.ObservableField;
import android.os.Parcel;

import org.parceler.ParcelConverter;
import org.parceler.Parcels;

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
public class ObservableFieldParcelConverter implements ParcelConverter<ObservableField> {

    @Override
    public void toParcel(ObservableField input, Parcel parcel) {
        Object value = input == null ? null : input.get();
        boolean isNonNull = value != null;
        parcel.writeByte((byte) (isNonNull ? 1 : 0));
        if(isNonNull) { parcel.writeParcelable(Parcels.wrap(value), 0); }
    }

    @Override
    public ObservableField fromParcel(Parcel parcel) {
        boolean isNonNull = parcel.readByte() == 1;
        return new ObservableField<>(isNonNull ? Parcels.unwrap(parcel.readParcelable(getClass().getClassLoader())) : null);
    }
}