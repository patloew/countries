package com.patloew.countries;

import com.patloew.countries.data.local.CountryRepo;
import com.patloew.countries.data.model.Country;
import com.patloew.countries.ui.main.viewpager.CountriesMvvm;
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

import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
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
@PrepareOnlyThisForTest(RealmResults.class)
public class FavoriteCountriesViewModelUnitTest {

    @Rule
    RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

    @Mock
    CountryRepo countryRepo;

    @Mock
    CountriesMvvm.View mainActivityView;
    FavoriteCountriesViewModel favoriteCountriesViewModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        favoriteCountriesViewModel = new FavoriteCountriesViewModel(countryRepo);

    }

    @Test
    public void onRealmChangeListener_twoTimes() {
        RealmResults<Country> countryList = PowerMockito.mock(RealmResults.class);

        doAnswer(invocation -> {
            RealmChangeListener realmChangeListener = (RealmChangeListener) invocation.getArguments()[2];
            realmChangeListener.onChange(null);
            realmChangeListener.onChange(null);
            return countryList;
        }).when(countryRepo).findAllSortedWithListener(Matchers.anyString(), Matchers.any(Sort.class), Matchers.any(RealmChangeListener.class));

        favoriteCountriesViewModel.attachView(mainActivityView, null);

        verify(mainActivityView, times(3)).onRefresh(eq(true), Matchers.anyList());

        favoriteCountriesViewModel.detachView();

        verify(countryList, times(1)).removeChangeListeners();
    }

    @Test
    public void onRealmChangeListener_never() {
        RealmResults<Country> countryList = PowerMockito.mock(RealmResults.class);

        doReturn(countryList).when(countryRepo).findAllSortedWithListener(Matchers.anyString(), Matchers.any(Sort.class), Matchers.any(RealmChangeListener.class));

        favoriteCountriesViewModel.attachView(mainActivityView, null);

        verify(mainActivityView, times(1)).onRefresh(eq(true), Matchers.anyList());

        favoriteCountriesViewModel.detachView();

        verify(countryList, times(1)).removeChangeListeners();
    }
}
