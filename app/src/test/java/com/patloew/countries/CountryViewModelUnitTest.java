package com.patloew.countries;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;

import com.patloew.countries.data.local.CountryRepo;
import com.patloew.countries.data.model.Country;
import com.patloew.countries.ui.base.MvvmView;
import com.patloew.countries.ui.main.recyclerview.CountryViewModelImpl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class CountryViewModelUnitTest {

    @Rule RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

    @Mock Context ctx;
    @Mock PackageManager packageManager;
    @Mock CountryRepo countryRepo;
    @Mock View view;

    @Mock MvvmView mvvmView;
    CountryViewModelImpl countryViewModel;

    Country internalCountry = new Country();

    @Before
    public void setup() throws PackageManager.NameNotFoundException {
        MockitoAnnotations.initMocks(this);

        when(ctx.getApplicationContext()).thenReturn(ctx);
        when(ctx.getPackageManager()).thenReturn(packageManager);
        //noinspection WrongConstant
        when(packageManager.getPackageInfo(Matchers.anyString(), Matchers.anyInt())).thenReturn(null);

        countryViewModel = new CountryViewModelImpl(ctx, countryRepo);
        countryViewModel.attachView(mvvmView, null);

        Whitebox.setInternalState(countryViewModel, "country", internalCountry);
    }

    @Test
    public void onMapClick_startActivity() {
        countryViewModel.onMapClick(view);
        verify(ctx).startActivity(Matchers.any(Intent.class));
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
        verify(countryRepo).save(internalCountry);
    }
}
