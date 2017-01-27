package com.patloew.template.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class Utils {

    // Usage: createViewHolder(viewGroup, R.layout.my_layout, MyViewHolder::new);
    public static <T extends RecyclerView.ViewHolder> T createViewHolder(@NonNull ViewGroup viewGroup, @LayoutRes int layoutResId, @NonNull PlainFunction<View, T> newViewHolderAction) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(layoutResId, viewGroup, false);

        return newViewHolderAction.apply(view);
    }

    // Tries to cast an Activity Context to another type
    @SuppressWarnings("unchecked")
    @Nullable
    public static <T> T castActivityFromContext(Context context, Class<T> castClass) {
        if(castClass.isInstance(context)) {
            return (T) context;
        }

        while(context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();

            if(castClass.isInstance(context)) {
                return (T) context;
            }
        }

        return null;
    }
}

