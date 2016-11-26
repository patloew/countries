package com.patloew.countries.ui.detail;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;

import com.patloew.countries.R;
import com.patloew.countries.data.local.CountryRepo;
import com.patloew.countries.data.model.Country;
import com.patloew.countries.data.model.RealmString;
import com.patloew.countries.data.model.RealmStringMapEntry;
import com.patloew.countries.data.remote.CountryApi;
import com.patloew.countries.injection.qualifier.AppContext;
import com.patloew.countries.injection.scopes.PerActivity;
import com.patloew.countries.ui.BaseCountryViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;
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

    private final ObservableField<CharSequence> borders = new ObservableField<>();
    private final ObservableField<CharSequence> currencies = new ObservableField<>();
    private final ObservableField<CharSequence> languages = new ObservableField<>();
    private final ObservableField<CharSequence> nameTranslations = new ObservableField<>();

    private final ObservableBoolean loaded = new ObservableBoolean();

    @Inject
    public DetailViewModel(@AppContext Context context, CountryRepo countryRepo, CountryApi countryApi) {
        super(context, countryRepo);
        this.countryApi = countryApi;
    }

    @Override
    public void saveInstanceState(@NonNull Bundle outState) {
        outState.putCharSequence(KEY_BORDERLIST, borders.get());
    }

    @Override
    public void restoreInstanceState(@NonNull Bundle savedInstanceState) {
        if(savedInstanceState.containsKey(KEY_BORDERLIST)) {
            borders.set(savedInstanceState.getCharSequence(KEY_BORDERLIST));
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeSubscription.clear();
    }

    @Override
    public void update(Country country, boolean isLast) {
        super.update(country, isLast);

        compositeSubscription.clear();
        loadBorders();
        loadDataForField(nameTranslations, this::calculateNameTranslations);
        loadDataForField(currencies, this::calculateCurrencies);
        loadDataForField(languages, this::calculateLanguages);
    }

    private <T> void loadDataForField(ObservableField<T> field, Callable<T> producer) {
        compositeSubscription.add(
                Single.fromCallable(producer)
                        .subscribeOn(Schedulers.computation())
                        .subscribe(field::set, throwable -> {
                            Timber.e(throwable, "Could not load data for field");
                            field.set(null);
                        })
        );
    }

    private CharSequence calculateLanguages() {
        ArrayList<String> languageList = new ArrayList<>(country.languages.size());

        for(RealmString language : country.languages) {
            languageList.add(new Locale(language.value).getDisplayLanguage(DISPLAY_LOCALE));
        }

        Collections.sort(languageList);

        return new SpannableStringBuilder(ctx.getText(R.string.country_languages)).append(TextUtils.join(", ", languageList));
    }

    private CharSequence calculateNameTranslations() {
        ArrayList<String> nameList = new ArrayList<>(country.translations.size());

        for(RealmStringMapEntry entry : country.translations) {
            nameList.add(entry.value + " <i>(" + new Locale(entry.key).getDisplayLanguage(DISPLAY_LOCALE) + ")</i>");
        }

        Collections.sort(nameList);

        return new SpannableStringBuilder(ctx.getText(R.string.country_name_translations)).append(Html.fromHtml(TextUtils.join(", ", nameList)));
    }

    private CharSequence calculateCurrencies() {
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

        return new SpannableStringBuilder(ctx.getText(R.string.country_currencies)).append(TextUtils.join(", ", currenciesList));
    }

    private void loadBorders() {
        borders.set(null);

        if(country.borders.size() > 0) {
            compositeSubscription.add(
                    countryApi.getAllCountries()
                        .subscribe(this::calculateBorders, this::onLoadCountriesError)
            );

        } else {
            loaded.set(true);
        }
    }

    private void calculateBorders(List<Country> countryList) {
        if(countryList != null) {
            List<String> borderList = new ArrayList<>(country.borders.size());
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

            borders.set(new SpannableStringBuilder(ctx.getText(R.string.country_borders)).append(TextUtils.join(", ", borderList)));
        }

        loaded.set(true);
    }

    private void onLoadCountriesError(Throwable throwable) {
        Timber.e(throwable, "Could not load countries");
        loaded.set(true);
    }

    // Properties

    @Override
    public ObservableField<CharSequence> getNameTranslations() {
        return nameTranslations;
    }

    @Override
    public ObservableField<CharSequence> getLanguages() {
        return languages;
    }

    @Override
    public ObservableBoolean isLoaded() {
        return loaded;
    }

    @Override
    public ObservableField<CharSequence> getBorders() {
        return borders;
    }

    @Override
    public ObservableField<CharSequence> getCurrencies() {
        return currencies;
    }


}
