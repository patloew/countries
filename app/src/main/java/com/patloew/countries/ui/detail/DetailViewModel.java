package com.patloew.countries.ui.detail;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;

import com.patloew.countries.R;
import com.patloew.countries.data.local.CountryRepo;
import com.patloew.countries.data.model.Country;
import com.patloew.countries.data.model.RealmString;
import com.patloew.countries.data.model.RealmStringMapEntry;
import com.patloew.countries.data.remote.CountryApi;
import com.patloew.countries.injection.qualifier.AppContext;
import com.patloew.countries.injection.scopes.PerActivity;
import com.patloew.countries.ui.main.recyclerview.BaseCountryViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.Locale;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

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

@PerActivity
public class DetailViewModel extends BaseCountryViewModel<DetailMvvm.View> implements DetailMvvm.ViewModel {

    private static final String KEY_BORDERLIST = "borderList";

    private final CountryApi countryApi;

    private final CompositeSubscription compositeSubscription = new CompositeSubscription();

    private ArrayList<String> borderList = null;

    @Inject
    public DetailViewModel(@AppContext Context context, CountryRepo countryRepo, CountryApi countryApi) {
        super(context, countryRepo);
        this.countryApi = countryApi;
    }

    @Override
    public void saveInstanceState(@NonNull Bundle outState) {
        outState.putStringArrayList(KEY_BORDERLIST, borderList);
    }

    @Override
    public void restoreInstanceState(@NonNull Bundle savedInstanceState) {
        borderList = savedInstanceState.containsKey(KEY_BORDERLIST) ? savedInstanceState.getStringArrayList(KEY_BORDERLIST) : borderList;
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeSubscription.clear();
    }

    @Override
    public void update(Country country, boolean isLast) {
        super.update(country, isLast);

        if(borderList == null) {
            compositeSubscription.add(countryApi.getAllCountries()
                    .subscribe(countryList -> {
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
                    }, throwable -> Timber.e(throwable, "Could not load countries")));
        }
    }

    // Properties

    @Override
    public boolean getDetailVisibility() {
        return borderList != null;
    }

    @Override
    public CharSequence getNameTranslations() {
        ArrayList<String> nameList = new ArrayList<>(country.translations.size());

        for(RealmStringMapEntry entry : country.translations) {
            nameList.add(entry.value + " <i>(" + new Locale(entry.key).getDisplayLanguage(DISPLAY_LOCALE) + ")</i>");
        }

        Collections.sort(nameList);

        return Html.fromHtml(String.format(ctx.getString(R.string.country_name_translations), TextUtils.join(", ", nameList)));
    }

    @Override
    public CharSequence getLanguages() {
        ArrayList<String> languageList = new ArrayList<>(country.languages.size());

        for(RealmString language : country.languages) {
            languageList.add(new Locale(language.value).getDisplayLanguage(DISPLAY_LOCALE));
        }

        Collections.sort(languageList);

        return Html.fromHtml(String.format(ctx.getString(R.string.country_languages), TextUtils.join(", ", languageList)));
    }

    @Override
    public CharSequence getCurrencies() {
        ArrayList<String> currenciesList = new ArrayList<>(country.currencies.size());

        for (RealmString currencyRealmString : country.currencies) {
            String currencyString = currencyRealmString.value;
            if (Build.VERSION.SDK_INT >= 19) {
                try {
                    Currency currency = Currency.getInstance(currencyString);
                    String currencySymbol = currency.getSymbol();
                    if (!currencyString.equals(currencySymbol)) {
                        currencySymbol = currencyString + ", " + currencySymbol;
                    }
                    currenciesList.add(currency.getDisplayName(DISPLAY_LOCALE) + " (" + currencySymbol + ")");
                } catch (IllegalArgumentException ignore) {
                    currenciesList.add(currencyString);
                }
            } else {
                currenciesList.add(currencyString);
            }
        }

        Collections.sort(currenciesList);

        return Html.fromHtml(String.format(ctx.getString(R.string.country_currencies), TextUtils.join(", ", currenciesList)));
    }

    @Override
    public int getBorderVisibility() {
        return borderList == null ? View.GONE : View.VISIBLE;
    }

    @Override
    public CharSequence getBorders() {
        return borderList == null ? "" : Html.fromHtml(String.format(ctx.getString(R.string.country_borders), TextUtils.join(", ", borderList)));
    }

}
