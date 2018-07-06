package com.patloew.countries.ui.detail

import android.content.Context
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.TextUtils
import com.patloew.countries.R
import com.patloew.countries.data.local.CountryRepo
import com.patloew.countries.data.model.Country
import com.patloew.countries.data.remote.CountryApi
import com.patloew.countries.injection.qualifier.AppContext
import com.patloew.countries.injection.scopes.PerActivity
import com.patloew.countries.ui.BaseCountryViewModel
import com.patloew.countries.ui.base.navigator.Navigator
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import javax.inject.Inject

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
 * FILE MODIFIED 2017 Tailored Media GmbH */

@PerActivity
class DetailViewModel
@Inject
constructor(@AppContext context: Context, countryRepo: CountryRepo, private val countryApi: CountryApi, navigator: Navigator) : BaseCountryViewModel<DetailMvvm.View>(context, countryRepo, navigator), DetailMvvm.ViewModel {

    companion object {
        private const val KEY_BORDER_LIST = "borderList"
    }

    private val compositeDisposable = CompositeDisposable()

    override val borders = ObservableField<CharSequence>()
    override val currencies = ObservableField<CharSequence>()
    override val languages = ObservableField<CharSequence>()

    override val nameTranslations = ObservableField<CharSequence>()

    override val isLoaded = ObservableBoolean()

    override fun saveInstanceState(outState: Bundle) {
        outState.putCharSequence(KEY_BORDER_LIST, borders.get())
    }

    public override fun restoreInstanceState(savedInstanceState: Bundle) {
        if (savedInstanceState.containsKey(KEY_BORDER_LIST)) {
            borders.set(savedInstanceState.getCharSequence(KEY_BORDER_LIST))
        }
    }

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    override fun update(country: Country, isLast: Boolean) {
        super.update(country, isLast)

        compositeDisposable.clear()
        loadBorders()
        loadDataForField(nameTranslations, { this.calculateNameTranslations() })
        loadDataForField(currencies, { this.calculateCurrencies() })
        loadDataForField(languages, { this.calculateLanguages() })
    }

    private fun <T> loadDataForField(field: ObservableField<T>, producer: () -> T) {
        compositeDisposable.add(
                Single.fromCallable(producer)
                        .subscribeOn(Schedulers.computation())
                        .subscribe({ field.set(it) }, { throwable ->
                            Timber.e(throwable, "Could not load data for field")
                            field.set(null)
                        })
        )
    }

    private fun calculateLanguages(): CharSequence {
        val languageList = ArrayList<String>(country.languages!!.size)

        country.languages!!.forEach {
            language ->

            languageList.add(Locale(language).getDisplayLanguage(BaseCountryViewModel.DISPLAY_LOCALE))
        }

        languageList.sort()

        return SpannableStringBuilder(ctx.getText(R.string.country_languages)).append(TextUtils.join(", ", languageList))
    }

    private fun calculateNameTranslations(): CharSequence {
        val nameList = ArrayList<String>(country.translations!!.size)

        country.translations!!.forEach { entry ->
            nameList.add(entry.value + " <i>(" + Locale(entry.key).getDisplayLanguage(BaseCountryViewModel.DISPLAY_LOCALE) + ")</i>")
        }

        nameList.sort()

        return SpannableStringBuilder(ctx.getText(R.string.country_name_translations)).append(Html.fromHtml(TextUtils.join(", ", nameList)))
    }

    private fun calculateCurrencies(): CharSequence {
        val currenciesList = ArrayList<String>(country.currencies!!.size)

        country.currencies!!.forEach {
            currencyString ->

            if (Build.VERSION.SDK_INT >= 19) {
                try {
                    val currency = Currency.getInstance(currencyString)
                    var currencySymbol = currency.symbol
                    if (currencyString != currencySymbol) {
                        currencySymbol = "$currencyString, $currencySymbol"
                    }
                    currenciesList.add("""${currency.getDisplayName(BaseCountryViewModel.DISPLAY_LOCALE)} ($currencySymbol)""")
                } catch (ignore: IllegalArgumentException) {
                    currenciesList.add(currencyString)
                }

            } else {
                currenciesList.add(currencyString)
            }
        }

        currenciesList.sort()

        return SpannableStringBuilder(ctx.getText(R.string.country_currencies)).append(TextUtils.join(", ", currenciesList))
    }

    private fun loadBorders() {
        borders.set(null)

        if (country.borders!!.size > 0) {
            compositeDisposable.add(
                    countryApi.getAllCountries()
                            .subscribe({ this.calculateBorders(it) },{ this.onLoadCountriesError(it) })
            )

        } else {
            isLoaded.set(true)
        }
    }

    private fun calculateBorders(countryList: List<Country>?) {
        if (countryList != null) {
            val borderList = ArrayList<String?>(country.borders!!.size)
            val alpha3List = ArrayList<String?>(country.borders!!.size)


            country.borders!!.forEach {
                borderAlpha3CodeString ->
                alpha3List.add(borderAlpha3CodeString)
            }

            for (c in countryList) {
                if (alpha3List.contains(c.alpha3Code)) {
                    borderList.add(c.name)
                    alpha3List.remove(c.alpha3Code)
                    if (alpha3List.isEmpty()) {
                        break
                    }
                }
            }

            borders.set(SpannableStringBuilder(ctx.getText(R.string.country_borders)).append(TextUtils.join(", ", borderList)))
        }

        isLoaded.set(true)
    }

    private fun onLoadCountriesError(throwable: Throwable) {
        Timber.e(throwable, "Could not load countries")
        isLoaded.set(true)
    }

}
