package com.patloew.countries.dagger;

import android.app.Application;
import android.view.View;

import com.patloew.countries.dagger.scopes.PerCountryViewHolder;
import com.patloew.countries.databinding.CardCountryBinding;
import com.patloew.countries.viewmodel.CountryViewModel;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

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
@Module
public class CountryViewHolderModule {

    private final View rootView;
    private final Realm realm;

    public CountryViewHolderModule(View rootView, Realm realm) {
        this.rootView = rootView;
        this.realm = realm;
    }

    @Provides
    @PerCountryViewHolder
    CardCountryBinding provideBinding() {
        return CardCountryBinding.bind(rootView);
    }

    @Provides
    @PerCountryViewHolder
    CountryViewModel provideViewModel(Application app, CardCountryBinding binding) {
        CountryViewModel vm = new CountryViewModel(app, realm, binding);
        binding.setVm(vm);
        return vm;
    }

}
