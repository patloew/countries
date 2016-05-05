package com.patloew.countries.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.AppCompatDrawableManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.patloew.countries.R;
import com.patloew.countries.data.local.CountryRepo;
import com.patloew.countries.data.model.Country;
import com.patloew.countries.data.model.RealmString;
import com.patloew.countries.data.model.RealmStringMapEntry;
import com.patloew.countries.injection.qualifier.AppContext;
import com.patloew.countries.injection.scopes.PerViewHolder;
import com.patloew.countries.ui.base.BaseViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
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
public class CountryViewModel extends BaseViewModel<CountryView> {

    private static final Locale DISPLAY_LOCALE = new Locale("EN");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat();

    private final Context ctx;
    private final CountryRepo countryRepo;
    private final boolean mapsAvailable;

    private Country country;
    private List<Country> countryList;
    private List<String> borderList;
    private int layoutPosition;

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

    public void onMapClick() {
        Country country = countryList.get(layoutPosition);
        Uri gmmIntentUri = Uri.parse("geo:"+country.lat+","+country.lng+"?q="+country.name+"&z=2");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(mapIntent);
    }

    public void onBookmarkClick() {
        Country realmCountry = countryRepo.getByField("alpha2Code", country.alpha2Code, false);

        if(realmCountry == null) {
            countryRepo.save(country);
            getView().setIsBookmarked(true);
        } else {
            countryRepo.delete(realmCountry);
            getView().setIsBookmarked(false);
        }
    }

    public void update(List<Country> countryList, int layoutPosition) {
        this.country = countryList.get(layoutPosition);
        this.countryList = countryList;
        this.layoutPosition = layoutPosition;

        if (country.borders.size() == 0 || countryList == null) {
            borderList = new ArrayList<>(0);
        } else {
            borderList = new ArrayList<>(country.borders.size());
            ArrayList<String> alpha3List = new ArrayList<>(country.borders.size());

            for (RealmString borderAlpha3CodeString : country.borders) {
                alpha3List.add(borderAlpha3CodeString.value);
            }

            for (Country c : countryList) {
                if (alpha3List.contains(c.alpha3Code)) {
                    borderList.add(c.name);
                    alpha3List.remove(c.alpha3Code);
                    if (alpha3List.isEmpty()) {
                        break;
                    }
                }
            }
        }

        notifyChange();
    }

    // Properties

    public String getName() {
        String nameInfo = country.name + " (" + country.alpha2Code;
        if(!country.name.equals(country.nativeName)) { nameInfo += ", " + country.nativeName; }
        return nameInfo + ")";
    }

    public CharSequence getNameTranslations() {
        ArrayList<String> nameList = new ArrayList<>(country.translations.size());

        for(RealmStringMapEntry entry : country.translations) {
            nameList.add(entry.value + " <i>(" + new Locale(entry.key).getDisplayLanguage(DISPLAY_LOCALE) + ")</i>");
        }

        Collections.sort(nameList);

        return Html.fromHtml(String.format(ctx.getString(R.string.country_name_translations), TextUtils.join(", ", nameList)));
    }

    public CharSequence getRegion() {
        return Html.fromHtml(String.format(ctx.getString(R.string.country_region), country.region));
    }

    public int getCapitalVisibility() {
        return TextUtils.isEmpty(country.capital) ? View.GONE : View.VISIBLE;
    }

    public CharSequence getCapital() {
        return Html.fromHtml(String.format(ctx.getString(R.string.country_capital), country.capital));
    }

    public CharSequence getPopulation() {
        return Html.fromHtml(String.format(ctx.getString(R.string.country_capital), DECIMAL_FORMAT.format(country.population)));
    }

    public CharSequence getLanguages() {
        ArrayList<String> languageList = new ArrayList<>(country.languages.size());

        for(RealmString language : country.languages) {
            languageList.add(new Locale(language.value).getDisplayLanguage(DISPLAY_LOCALE));
        }

        Collections.sort(languageList);

        return Html.fromHtml(String.format(ctx.getString(R.string.country_languages), TextUtils.join(", ", languageList)));
    }

    public CharSequence getCurrencies() {
        ArrayList<String> currenciesList = new ArrayList<>(country.currencies.size());

        for(RealmString currencyRealmString : country.currencies) {
            String currencyString = currencyRealmString.value;
            if(Build.VERSION.SDK_INT >= 19) {
                try {
                    Currency currency = Currency.getInstance(currencyString);
                    String currencySymbol = currency.getSymbol();
                    if(!currencyString.equals(currencySymbol)) { currencySymbol = currencyString + ", " + currencySymbol;}
                    currenciesList.add(currency.getDisplayName(DISPLAY_LOCALE) + " (" + currencySymbol + ")");
                } catch(IllegalArgumentException ignore) {
                    currenciesList.add(currencyString);
                }
            } else {
                currenciesList.add(currencyString);
            }
        }

        Collections.sort(currenciesList);

        return Html.fromHtml(String.format(ctx.getString(R.string.country_currencies), TextUtils.join(", ", currenciesList)));
    }

    public int getLocationVisibility() {
        return country.lat == null && country.lng == null ? View.GONE : View.VISIBLE;
    }

    public CharSequence getLocation() {
        if(getLocationVisibility() == View.VISIBLE) {
            return Html.fromHtml(String.format(ctx.getString(R.string.country_location), DECIMAL_FORMAT.format(country.lat), DECIMAL_FORMAT.format(country.lng)));
        } else {
            return null;
        }
    }

    public int getBorderVisibility() {
        return borderList.isEmpty() ? View.GONE : View.VISIBLE;
    }

    public CharSequence getBorders() {
        return Html.fromHtml(String.format(ctx.getString(R.string.country_borders), TextUtils.join(", ", borderList)));
    }

    public Drawable getBookmarkDrawable() {
        return AppCompatDrawableManager.get().getDrawable(ctx, countryRepo.getByField("alpha2Code", country.alpha2Code, false) != null ? R.drawable.ic_bookmark_black : R.drawable.ic_bookmark_border_black);
    }

    public int getMapVisibility() {
        return mapsAvailable && country.lat != null && country.lng != null ? View.VISIBLE : View.GONE;
    }

    public int getCardBottomMargin() {
        return layoutPosition == countryList.size()-1 ? (int) ctx.getResources().getDimension(R.dimen.card_outer_padding) : 0;
    }

    @BindingAdapter("android:layout_marginBottom")
    public static void setLayoutMarginBottom(View v, int bottomMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        if (layoutParams != null) { layoutParams.bottomMargin = bottomMargin; }
    }
}
