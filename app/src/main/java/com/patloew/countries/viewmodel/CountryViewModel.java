package com.patloew.countries.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
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
import com.patloew.countries.model.Country;
import com.patloew.countries.model.RealmString;
import com.patloew.countries.model.RealmStringMapEntry;
import com.patloew.countries.view.CountryView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;

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
public class CountryViewModel extends BaseObservable {

    private static final Locale DISPLAY_LOCALE = new Locale("EN");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat();

    private final Context ctx;
    private final Realm realm;
    private final CountryView countryView;
    private final boolean mapsAvailable;

    private Country country;
    private List<Country> countryList;
    private List<String> borderList;
    private int layoutPosition;

    public CountryViewModel(Context context, Realm realm, CountryView countryView, boolean mapsAvailable) {
        this.ctx = context.getApplicationContext();
        this.realm = realm;
        this.countryView = countryView;
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
        Country realmCountry = realm.where(Country.class).equalTo("alpha2Code", country.alpha2Code).findFirst();

        realm.beginTransaction();

        if(realmCountry == null) {
            realm.copyToRealmOrUpdate(country);
            countryView.setIsBookmarked(true);
        } else {
            // delete Country and all referenced RealmObjects
            realmCountry.borders.deleteAllFromRealm();
            realmCountry.currencies.deleteAllFromRealm();
            realmCountry.languages.deleteAllFromRealm();
            realmCountry.translations.deleteAllFromRealm();
            realmCountry.removeFromRealm();

            countryView.setIsBookmarked(false);
        }

        realm.commitTransaction();
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
        return AppCompatDrawableManager.get().getDrawable(ctx, realm.where(Country.class).equalTo("alpha2Code", country.alpha2Code).findFirst() != null ? R.drawable.ic_bookmark_black : R.drawable.ic_bookmark_border_black);
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
