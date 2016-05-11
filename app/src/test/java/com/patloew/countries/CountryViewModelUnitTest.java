package com.patloew.countries;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.patloew.countries.data.local.CountryRepo;
import com.patloew.countries.data.model.Country;
import com.patloew.countries.ui.main.CountryView;
import com.patloew.countries.ui.main.CountryViewModel;

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

    @Mock CountryView countryView;
    CountryViewModel countryViewModel;

    Country internalCountry = new Country();

    @Before
    public void setup() throws PackageManager.NameNotFoundException {
        MockitoAnnotations.initMocks(this);

        when(ctx.getApplicationContext()).thenReturn(ctx);
        when(ctx.getPackageManager()).thenReturn(packageManager);
        //noinspection WrongConstant
        when(packageManager.getPackageInfo(Matchers.anyString(), Matchers.anyInt())).thenReturn(null);

        countryViewModel = new CountryViewModel(ctx, countryRepo);
        countryViewModel.attachView(countryView, null);

        Whitebox.setInternalState(countryViewModel, "country", internalCountry);
    }


    @Test
    public void onMapClick_startActivity() {
        countryViewModel.onMapClick();
        verify(ctx).startActivity(Matchers.any(Intent.class));
    }

    @Test
    public void onBookmarkClick_wasBookmarked() {
        Country country = new Country();
        doReturn(country).when(countryRepo).getByField(Matchers.anyString(), Matchers.anyString(), Matchers.anyBoolean());

        countryViewModel.onBookmarkClick();
        verify(countryRepo).delete(country);
        verify(countryView).setIsBookmarked(false);
    }

    @Test
    public void onBookmarkClick_wasNotBookmarked() {
        doReturn(null).when(countryRepo).getByField(Matchers.anyString(), Matchers.anyString(), Matchers.anyBoolean());

        countryViewModel.onBookmarkClick();
        verify(countryRepo).save(internalCountry);
        verify(countryView).setIsBookmarked(true);
    }
}
