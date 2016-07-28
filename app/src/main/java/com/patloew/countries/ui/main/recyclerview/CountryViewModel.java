package com.patloew.countries.ui.main.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.AppCompatDrawableManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.patloew.countries.BR;
import com.patloew.countries.R;
import com.patloew.countries.data.local.CountryRepo;
import com.patloew.countries.data.model.Country;
import com.patloew.countries.injection.qualifier.AppContext;
import com.patloew.countries.injection.scopes.PerViewHolder;
import com.patloew.countries.ui.base.BaseViewModel;
import com.patloew.countries.ui.base.MvvmView;

import java.text.DecimalFormat;
import java.util.Locale;

import javax.inject.Inject;

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
@PerViewHolder
public class CountryViewModel extends BaseViewModel<MvvmView> implements CountryMvvm.ViewModel {

    private static final Locale DISPLAY_LOCALE = new Locale("EN");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat();

    private final Context ctx;
    private final CountryRepo countryRepo;
    private final boolean mapsAvailable;

    private Country country;
    private boolean isLast = false;

    @Inject
    public CountryViewModel(@AppContext Context context, CountryRepo countryRepo) {
        this.ctx = context.getApplicationContext();
        this.countryRepo = countryRepo;

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
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(mapIntent);
    }

    @Override
    public void onBookmarkClick(View view) {
        Country realmCountry = countryRepo.getByField("alpha2Code", country.alpha2Code, false);

        if(realmCountry == null) {
            countryRepo.update(country);
        } else {
            country = countryRepo.detach(country);
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
    public String getName() {
        String nameInfo = country.name + " (" + country.alpha2Code;
        if(!country.name.equals(country.nativeName)) { nameInfo += ", " + country.nativeName; }
        return nameInfo + ")";
    }

    @Override
    public CharSequence getRegion() {
        return Html.fromHtml(String.format(ctx.getString(R.string.country_region), country.region));
    }

    @Override
    public int getCapitalVisibility() {
        return TextUtils.isEmpty(country.capital) ? View.GONE : View.VISIBLE;
    }

    @Override
    public CharSequence getCapital() {
        return Html.fromHtml(String.format(ctx.getString(R.string.country_capital), country.capital));
    }

    @Override
    public CharSequence getPopulation() {
        return Html.fromHtml(String.format(ctx.getString(R.string.country_population), DECIMAL_FORMAT.format(country.population)));
    }

    @Override
    public int getLocationVisibility() {
        return country.lat == null && country.lng == null ? View.GONE : View.VISIBLE;
    }

    @Override
    public CharSequence getLocation() {
        if(getLocationVisibility() == View.VISIBLE) {
            return Html.fromHtml(String.format(ctx.getString(R.string.country_location), DECIMAL_FORMAT.format(country.lat), DECIMAL_FORMAT.format(country.lng)));
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
    public int getMapVisibility() {
        return mapsAvailable && country.lat != null && country.lng != null ? View.VISIBLE : View.GONE;
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
