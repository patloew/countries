package com.patloew.countries.ui.main.viewpager.favorites;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.patloew.countries.data.local.CountryRepo;
import com.patloew.countries.data.model.Country;
import com.patloew.countries.injection.scopes.PerFragment;
import com.patloew.countries.ui.base.viewmodel.BaseViewModel;
import com.patloew.countries.ui.main.recyclerview.CountryAdapter;
import com.patloew.countries.ui.main.viewpager.CountriesView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.realm.Sort;
import timber.log.Timber;

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
public class FavoriteCountriesViewModel extends BaseViewModel<CountriesView> implements IFavoriteCountriesViewModel {

    private final CountryAdapter adapter;
    private final CountryRepo countryRepo;
    private Disposable disposable;

    @Inject
    public FavoriteCountriesViewModel(CountryAdapter adapter, CountryRepo countryRepo) {
        this.adapter = adapter;
        this.countryRepo = countryRepo;
    }

    @Override
    public void attachView(@NonNull CountriesView view, @Nullable Bundle savedInstanceState) {
        super.attachView(view, savedInstanceState);

        disposable = countryRepo.findAllSortedWithChanges("name", Sort.ASCENDING)
                .subscribe(this::refreshView, Timber::e);
    }

    private void refreshView(List<Country> countryList) {
        adapter.setCountryList(countryList);
        adapter.notifyDataSetChanged();
        getView().onRefresh(true);
    }

    @Override
    public void detachView() {
        super.detachView();

        if(disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

    @Override
    public CountryAdapter getAdapter() {
        return adapter;
    }
}
