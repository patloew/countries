package com.tailoredapps.template.util;

import android.databinding.ObservableDouble;
import android.os.Parcel;

import org.parceler.ParcelConverter;

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
public class ObservableDoubleParcelConverter implements ParcelConverter<ObservableDouble> {

    @Override
    public void toParcel(ObservableDouble input, Parcel parcel) {
        parcel.writeDouble(input.get());
    }

    @Override
    public ObservableDouble fromParcel(Parcel parcel) {
        return new ObservableDouble(parcel.readDouble());
    }
}