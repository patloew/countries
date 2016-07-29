package com.patloew.countries;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;

import com.patloew.countries.data.local.CountryRepo;
import com.patloew.countries.data.model.Country;
import com.patloew.countries.ui.base.navigator.Navigator;
import com.patloew.countries.ui.base.view.MvvmView;
import com.patloew.countries.ui.main.recyclerview.CountryViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import javax.inject.Provider;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
public class BaseCountryViewModelUnitTest {

    @Rule RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

    @Mock Context ctx;
    @Mock PackageManager packageManager;
    @Mock CountryRepo countryRepo;
    @Mock View view;

    @Mock MvvmView mvvmView;
    @Mock Provider<Navigator> navigatorProvider;
    @Mock Navigator navigator;
    CountryViewModel countryViewModel;


    Country internalCountry = new Country();

    @Before
    public void setup() throws PackageManager.NameNotFoundException {
        MockitoAnnotations.initMocks(this);

        when(navigatorProvider.get()).thenReturn(navigator);
        when(ctx.getApplicationContext()).thenReturn(ctx);
        when(ctx.getPackageManager()).thenReturn(packageManager);
        //noinspection WrongConstant
        when(packageManager.getPackageInfo(Matchers.anyString(), Matchers.anyInt())).thenReturn(null);

        countryViewModel = new CountryViewModel(ctx, countryRepo);
        countryViewModel.attachView(mvvmView, null);

        Whitebox.setInternalState(countryViewModel, "country", internalCountry);
        Whitebox.setInternalState(countryViewModel, "navigator", navigatorProvider);
    }

    @Test
    public void onMapClick_startActivity() {
        countryViewModel.onMapClick(view);
        verify(navigator).startActivity(Matchers.eq(Intent.ACTION_VIEW), Matchers.any(Uri.class));
    }

    @Test
    public void onBookmarkClick_wasBookmarked() {
        Country country = new Country();
        doReturn(country).when(countryRepo).getByField(Matchers.anyString(), Matchers.anyString(), Matchers.anyBoolean());

        countryViewModel.onBookmarkClick(view);
        verify(countryRepo).delete(country);
    }

    @Test
    public void onBookmarkClick_wasNotBookmarked() {
        doReturn(null).when(countryRepo).getByField(Matchers.anyString(), Matchers.anyString(), Matchers.anyBoolean());

        countryViewModel.onBookmarkClick(view);
        verify(countryRepo).update(internalCountry);
    }
}
