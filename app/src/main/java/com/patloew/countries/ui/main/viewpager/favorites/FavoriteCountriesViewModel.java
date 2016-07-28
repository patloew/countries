package com.patloew.countries.ui.main.viewpager.favorites;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.patloew.countries.data.local.CountryRepo;
import com.patloew.countries.data.model.Country;
import com.patloew.countries.injection.scopes.PerFragment;
import com.patloew.countries.ui.base.BaseViewModel;
import com.patloew.countries.ui.main.viewpager.CountriesMvvm;

import javax.inject.Inject;

import io.realm.RealmResults;
import io.realm.Sort;

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

@PerFragment
public class FavoriteCountriesViewModel extends BaseViewModel<CountriesMvvm.View> implements IFavoriteCountriesViewModel {

    private final CountryRepo countryRepo;

    private RealmResults<Country> countryList;

    @Inject
    public FavoriteCountriesViewModel(CountryRepo countryRepo) {
        this.countryRepo = countryRepo;
    }

    @Override
    public void attachView(@NonNull CountriesMvvm.View view, @Nullable Bundle savedInstanceState) {
        super.attachView(view, savedInstanceState);
        countryList = countryRepo.findAllSortedWithListener("name", Sort.ASCENDING, element -> getView().onRefresh(true, countryList));
        getView().onRefresh(true, countryList);
    }

    @Override
    public void detachView() {
        super.detachView();
        if(countryList != null) { countryList.removeChangeListeners(); }
    }
}
