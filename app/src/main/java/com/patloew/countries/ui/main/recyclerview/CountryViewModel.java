package com.patloew.countries.ui.main.recyclerview;

import android.databinding.Bindable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.patloew.countries.data.model.Country;
import com.patloew.countries.ui.base.MvvmView;
import com.patloew.countries.ui.base.ViewModel;

import java.util.List;

public interface CountryViewModel extends ViewModel<MvvmView> {

    void onMapClick(View view);
    void onBookmarkClick(View view);
    void update(List<Country> countryList, int layoutPosition);

    // Properties

    String getName();
    CharSequence getNameTranslations();
    CharSequence getRegion();
    int getCapitalVisibility();
    CharSequence getCapital();
    CharSequence getPopulation();
    CharSequence getLanguages();
    CharSequence getCurrencies();
    int getLocationVisibility();
    CharSequence getLocation();
    int getBorderVisibility();
    CharSequence getBorders();
    @Bindable
    Drawable getBookmarkDrawable();
    int getMapVisibility();
    int getCardBottomMargin();

}
