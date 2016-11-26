package com.patloew.countries.ui;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.patloew.countries.data.model.Country;
import com.patloew.countries.ui.base.view.MvvmView;
import com.patloew.countries.ui.base.viewmodel.MvvmViewModel;

public interface ICountryViewModel<V extends MvvmView> extends MvvmViewModel<V> {

    void onMapClick(View view);
    void onBookmarkClick(View view);
    void update(Country country, boolean isLast);

    // Properties

    Country getCountry();
    String getName();
    CharSequence getRegion();
    boolean isCapitalVisible();
    CharSequence getCapital();
    CharSequence getPopulation();
    boolean isLocationVisible();
    CharSequence getLocation();
    Drawable getBookmarkDrawable();
    boolean isMapVisible();
    int getCardBottomMargin();

}
