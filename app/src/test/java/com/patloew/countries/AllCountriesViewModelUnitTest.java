package com.patloew.countries;

import com.patloew.countries.data.model.Country;
import com.patloew.countries.data.remote.CountryApi;
import com.patloew.countries.ui.main.viewpager.CountriesMvvm;
import com.patloew.countries.ui.main.viewpager.all.AllCountriesViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static org.mockito.Matchers.eq;
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
public class AllCountriesViewModelUnitTest {

    @Rule RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

    @Mock CountryApi countryApi;

    @Mock CountriesMvvm.View countriesView;
    AllCountriesViewModel allCountriesViewModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        allCountriesViewModel = new AllCountriesViewModel(countryApi);
        allCountriesViewModel.attachView(countriesView, null);
    }

    @Test
    public void onRefresh_success() {
        List<Country> countryList = new ArrayList<>();
        countryList.add(new Country());

        doReturn(Observable.just(countryList)).when(countryApi).getAllCountries();

        allCountriesViewModel.onRefresh(false);

        verify(countriesView, times(1)).onRefresh(eq(true), Matchers.anyList());
    }

    @Test
    public void onRefresh_error() {
        doReturn(Observable.error(new RuntimeException("Error getting countries"))).when(countryApi).getAllCountries();

        allCountriesViewModel.onRefresh(false);

        verify(countriesView, times(1)).onRefresh(eq(false), Matchers.anyList());
    }
}
