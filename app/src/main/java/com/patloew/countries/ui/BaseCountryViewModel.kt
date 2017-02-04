package com.patloew.countries.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.Bindable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v7.widget.AppCompatDrawableManager
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.view.View
import com.patloew.countries.BR
import com.patloew.countries.R
import com.patloew.countries.data.local.CountryRepo
import com.patloew.countries.data.model.Country
import com.patloew.countries.injection.qualifier.AppContext
import com.patloew.countries.ui.base.navigator.Navigator
import com.patloew.countries.ui.base.view.MvvmView
import com.patloew.countries.ui.base.viewmodel.BaseViewModel
import java.text.DecimalFormat
import java.util.*

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
abstract class BaseCountryViewModel<V : MvvmView>(@AppContext context: Context, protected val countryRepo: CountryRepo, protected val navigator: Navigator) : BaseViewModel<V>(), ICountryViewModel<V> {

    companion object {
        val DISPLAY_LOCALE = Locale("EN")
        val DECIMAL_FORMAT = DecimalFormat()
    }

    protected val ctx: Context = context.applicationContext
    protected val mapsAvailable: Boolean


    override lateinit var country: Country
        protected set

    private var isLast = false

    init {
        var mapsAvailable = false

        try {
            context.packageManager.getPackageInfo("com.google.android.apps.maps", 0)
            mapsAvailable = true
        } catch (ignore: PackageManager.NameNotFoundException) { }

        this.mapsAvailable = mapsAvailable
    }

    override fun onMapClick(view: View?) {
        val gmmIntentUri = Uri.parse("geo:" + country.lat + "," + country.lng + "?q=" + country.name + "&z=2")
        navigator.startActivity(Intent.ACTION_VIEW, gmmIntentUri)
    }

    override fun onBookmarkClick(view: View?) {
        val realmCountry = countryRepo.getByField("alpha2Code", country.alpha2Code, false)

        if (realmCountry == null) {
            countryRepo.save(country)
        } else {
            country = countryRepo.detach(realmCountry)
            countryRepo.delete(realmCountry)
        }

        notifyPropertyChanged(BR.bookmarkDrawable)
    }

    override fun update(country: Country, isLast: Boolean) {
        this.isLast = isLast
        this.country = country

        notifyChange()
    }

    override val name: String
        get() {
            var nameInfo = country.name + " (" + country.alpha2Code
            if (country.name != country.nativeName) {
                nameInfo += ", " + country.nativeName
            }
            return nameInfo + ")"
        }

    override val region: CharSequence
        get() = SpannableStringBuilder(ctx.getText(R.string.country_region))
                .append(country.region)

    override val isCapitalVisible: Boolean
        get() = !TextUtils.isEmpty(country.capital)

    override val capital: CharSequence
        get() = SpannableStringBuilder(ctx.getText(R.string.country_capital))
                .append(country.capital)

    override val population: CharSequence
        get() = SpannableStringBuilder(ctx.getText(R.string.country_population))
                .append(DECIMAL_FORMAT.format(country.population))

    override val isLocationVisible: Boolean
        get() = country.lat != null && country.lng != null

    override val location: CharSequence
        get() {
            if (isLocationVisible) {
                return SpannableStringBuilder(ctx.getText(R.string.country_location))
                        .append(DECIMAL_FORMAT.format(country.lat))
                        .append(", ")
                        .append(DECIMAL_FORMAT.format(country.lng))
            } else {
                return ""
            }
        }

    override val bookmarkDrawable: Drawable
        @Bindable
        get() = AppCompatDrawableManager.get().getDrawable(ctx, if (countryRepo.getByField("alpha2Code", country.alpha2Code!!, false) != null) R.drawable.ic_bookmark_black else R.drawable.ic_bookmark_border_black)

    override val isMapVisible: Boolean
        get() = mapsAvailable && country.lat != null && country.lng != null

    override val cardBottomMargin: Int
        get() = if (isLast) ctx.resources.getDimension(R.dimen.card_outer_padding).toInt() else 0
}
