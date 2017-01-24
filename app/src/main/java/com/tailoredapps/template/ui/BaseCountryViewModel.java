package com.tailoredapps.template.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.AppCompatDrawableManager;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.tailoredapps.template.BR;
import com.tailoredapps.template.R;
import com.tailoredapps.template.data.local.CountryRepo;
import com.tailoredapps.template.data.model.Country;
import com.tailoredapps.template.injection.qualifier.AppContext;
import com.tailoredapps.template.ui.base.navigator.Navigator;
import com.tailoredapps.template.ui.base.view.MvvmView;
import com.tailoredapps.template.ui.base.viewmodel.BaseViewModel;

import java.text.DecimalFormat;
import java.util.Locale;

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
 * limitations under the License.
 *
 * -------
 *
 * FILE MODIFIED 2017 Tailored Media GmbH
 * */
public abstract class BaseCountryViewModel<V extends MvvmView> extends BaseViewModel<V> implements ICountryViewModel<V> {

    protected static final Locale DISPLAY_LOCALE = new Locale("EN");
    protected static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat();

    protected final Context ctx;
    protected final CountryRepo countryRepo;
    protected final Navigator navigator;
    protected final boolean mapsAvailable;

    protected Country country;
    private boolean isLast = false;

    public BaseCountryViewModel(@AppContext Context context, CountryRepo countryRepo, Navigator navigator) {
        this.ctx = context.getApplicationContext();
        this.countryRepo = countryRepo;
        this.navigator = navigator;

        boolean mapsAvailable = false;

        try {
            context.getPackageManager().getPackageInfo("com.google.android.apps.maps", 0);
            mapsAvailable = true;
        } catch(PackageManager.NameNotFoundException ignore) {}

        this.mapsAvailable = mapsAvailable;
    }

    @Override
    public void onMapClick(View view) {
        Uri gmmIntentUri = Uri.parse("geo:"+country.lat+","+country.lng+"?q="+country.name+"&z=2");
        navigator.startActivity(Intent.ACTION_VIEW, gmmIntentUri);
    }

    @Override
    public void onBookmarkClick(View view) {
        Country realmCountry = countryRepo.getByField("alpha2Code", country.alpha2Code, false);

        if(realmCountry == null) {
            countryRepo.save(country);
        } else {
            country = countryRepo.detach(realmCountry);
            countryRepo.delete(realmCountry);
        }

        notifyPropertyChanged(BR.bookmarkDrawable);
    }

    @Override
    public void update(Country country, boolean isLast) {
        this.isLast = isLast;
        this.country = country;

        notifyChange();
    }

    // Properties


    @Override
    public Country getCountry() {
        return country;
    }

    @Override
    public String getName() {
        String nameInfo = country.name + " (" + country.alpha2Code;
        if(!country.name.equals(country.nativeName)) { nameInfo += ", " + country.nativeName; }
        return nameInfo + ")";
    }

    @Override
    public CharSequence getRegion() {
        return new SpannableStringBuilder(ctx.getText(R.string.country_region))
                .append(country.region);
    }

    @Override
    public boolean isCapitalVisible() {
        return !TextUtils.isEmpty(country.capital);
    }

    @Override
    public CharSequence getCapital() {
        return new SpannableStringBuilder(ctx.getText(R.string.country_capital))
                .append(country.capital);
    }

    @Override
    public CharSequence getPopulation() {
        return new SpannableStringBuilder(ctx.getText(R.string.country_population))
                .append(DECIMAL_FORMAT.format(country.population));
    }

    @Override
    public boolean isLocationVisible() {
        return country.lat != null && country.lng != null;
    }

    @Override
    public CharSequence getLocation() {
        if(isLocationVisible()) {
            return new SpannableStringBuilder(ctx.getText(R.string.country_location))
                    .append(DECIMAL_FORMAT.format(country.lat))
                    .append(", ")
                    .append(DECIMAL_FORMAT.format(country.lng));
        } else {
            return null;
        }
    }

    @Override
    @Bindable
    public Drawable getBookmarkDrawable() {
        return AppCompatDrawableManager.get().getDrawable(ctx, countryRepo.getByField("alpha2Code", country.alpha2Code, false) != null ? R.drawable.ic_bookmark_black : R.drawable.ic_bookmark_border_black);
    }

    @Override
    public boolean isMapVisible() {
        return mapsAvailable && country.lat != null && country.lng != null;
    }

    @Override
    public int getCardBottomMargin() {
        return isLast ? (int) ctx.getResources().getDimension(R.dimen.card_outer_padding) : 0;
    }

    @BindingAdapter("android:layout_marginBottom")
    public static void setLayoutMarginBottom(View v, int bottomMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        if (layoutParams != null) { layoutParams.bottomMargin = bottomMargin; }
    }
}
