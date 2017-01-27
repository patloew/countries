package com.patloew.countries;

import android.support.v7.widget.RecyclerView;

import com.patloew.countries.data.local.CountryRepo;
import com.patloew.countries.data.model.Country;
import com.patloew.countries.ui.main.recyclerview.CountryAdapter;
import com.patloew.countries.ui.main.viewpager.CountriesView;
import com.patloew.countries.ui.main.viewpager.favorites.FavoriteCountriesViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.realm.RealmResults;
import io.realm.Sort;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({ RealmResults.class, RecyclerView.Adapter.class })
public class FavoriteCountriesViewModelUnitTest {

    @Rule
    RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

    @Mock CountryRepo countryRepo;
    CountryAdapter adapter;

    @Mock CountriesView mainActivityView;
    FavoriteCountriesViewModel favoriteCountriesViewModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        adapter = PowerMockito.mock(CountryAdapter.class);
        PowerMockito.doAnswer(invocation -> null).when((RecyclerView.Adapter)adapter).notifyDataSetChanged();

        favoriteCountriesViewModel = new FavoriteCountriesViewModel(adapter, countryRepo);
    }

    @Test
    public void onRealmChangeListener_threeTimes() {
        List<Country> countryList = new ArrayList<>(0);

        doReturn(Observable.just(countryList, countryList, countryList)).when(countryRepo).findAllSortedWithChanges(Matchers.anyString(), Matchers.any(Sort.class));

        favoriteCountriesViewModel.attachView(mainActivityView, null);

        verify(adapter, times(3)).setCountryList(countryList);
        verify(adapter, times(3)).notifyDataSetChanged();
        verify(mainActivityView, times(3)).onRefresh(true);

        favoriteCountriesViewModel.detachView();
    }

    @Test
    public void onRealmChangeListener_never() {
        List<Country> countryList = new ArrayList<>(0);

        doReturn(Observable.empty()).when(countryRepo).findAllSortedWithChanges(Matchers.anyString(), Matchers.any(Sort.class));

        favoriteCountriesViewModel.attachView(mainActivityView, null);

        verify(adapter, never()).setCountryList(countryList);
        verify(adapter, never()).notifyDataSetChanged();
        verify(mainActivityView, never()).onRefresh(true);

        favoriteCountriesViewModel.detachView();
    }
}
