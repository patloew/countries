package com.patloew.countries;

import android.content.Context;

import com.patloew.countries.data.local.CountryRepo;
import com.patloew.countries.data.model.Country;
import com.patloew.countries.data.remote.CountryApi;
import com.patloew.countries.ui.main.MainActivityView;
import com.patloew.countries.ui.main.MainActivityViewModel;

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

import io.realm.Sort;
import rx.Observable;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class MainActivityViewModelUnitTest {

    @Rule RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

    @Mock Context ctx;
    @Mock CountryRepo countryRepo;
    @Mock CountryApi countryApi;

    @Mock MainActivityView mainActivityView;
    MainActivityViewModel mainActivityViewModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(ctx.getApplicationContext()).thenReturn(ctx);

        mainActivityViewModel = new MainActivityViewModel(ctx, countryRepo, countryApi);
        mainActivityViewModel.attachView(mainActivityView, null);
    }

    @Test
    public void onRefresh_initialLoading_success() {
        List<Country> countryList = new ArrayList<>();
        countryList.add(new Country());

        doReturn(countryList).when(countryRepo).findAllSorted(Matchers.anyString(), Matchers.any(Sort.class), Matchers.anyBoolean());
        doReturn(Observable.just(countryList)).when(countryApi).getAllCountries();

        mainActivityViewModel.onRefresh(true);

        verify(mainActivityView, times(2)).onRefresh(eq(true), Matchers.anyList());
    }

    @Test
    public void onRefresh_initialLoading_error() {
        List<Country> countryList = new ArrayList<>();
        countryList.add(new Country());

        doReturn(countryList).when(countryRepo).findAllSorted(Matchers.anyString(), Matchers.any(Sort.class), Matchers.anyBoolean());
        doReturn(Observable.error(new RuntimeException("Error getting countries"))).when(countryApi).getAllCountries();

        mainActivityViewModel.onRefresh(true);

        verify(mainActivityView, times(1)).onRefresh(true, countryList);
        verify(mainActivityView, times(1)).onRefresh(eq(false), Matchers.anyList());
    }

    @Test
    public void onRefresh_noInitialLoading_success() {
        List<Country> countryList = new ArrayList<>();
        countryList.add(new Country());

        doReturn(Observable.just(countryList)).when(countryApi).getAllCountries();

        mainActivityViewModel.onRefresh(false);

        verify(mainActivityView, times(1)).onRefresh(eq(true), Matchers.anyList());
    }

    @Test
    public void onRefresh_noInitialLoading_error() {
        doReturn(Observable.error(new RuntimeException("Error getting countries"))).when(countryApi).getAllCountries();

        mainActivityViewModel.onRefresh(false);

        verify(mainActivityView, times(1)).onRefresh(eq(false), Matchers.anyList());
    }
}
