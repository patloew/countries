package com.tailoredapps.template.util;

import android.os.Parcel;
import android.os.Parcelable;

import org.parceler.Parcels;
import org.parceler.TypeRangeParcelConverter;

import io.realm.RealmList;
import io.realm.RealmObject;

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
public class RealmListParcelConverter implements TypeRangeParcelConverter<RealmList<? extends RealmObject>, RealmList<? extends RealmObject>> {
  private static final int NULL = -1;

  @Override
  public void toParcel(RealmList<? extends RealmObject> input, Parcel parcel) {
    if (input == null) {
      parcel.writeInt(NULL);
    } else {
      parcel.writeInt(input.size());
      for (RealmObject item : input) {
        parcel.writeParcelable(Parcels.wrap(item), 0);
      }
    }
  }

  @Override
  public RealmList fromParcel(Parcel parcel) {
    int size = parcel.readInt();
    RealmList list = new RealmList();

    for (int i=0; i<size; i++) {
      Parcelable parcelable = parcel.readParcelable(getClass().getClassLoader());
      list.add((RealmObject) Parcels.unwrap(parcelable));
    }

    return list;
  }
}