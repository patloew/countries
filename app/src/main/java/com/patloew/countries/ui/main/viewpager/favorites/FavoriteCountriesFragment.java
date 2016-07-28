package com.patloew.countries.ui.main.viewpager.favorites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.patloew.countries.ui.main.viewpager.CountriesFragment;

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
public class FavoriteCountriesFragment extends CountriesFragment<IFavoriteCountriesViewModel> {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentComponent().inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.swipeRefreshLayout.setOnRefreshListener(() -> binding.swipeRefreshLayout.setRefreshing(false));
    }
}
