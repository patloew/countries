package com.patloew.countries.ui.main.viewpager.all

import android.os.Bundle
import com.patloew.countries.data.local.CountryRepo
import com.patloew.countries.data.remote.CountryApi
import com.patloew.countries.injection.scopes.PerFragment
import com.patloew.countries.ui.base.viewmodel.BaseViewModel
import com.patloew.countries.ui.main.recyclerview.CountryAdapter
import com.patloew.countries.ui.main.viewpager.CountriesView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
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
 * limitations under the License. */

@PerFragment
class AllCountriesViewModel
@Inject
constructor(override val adapter: CountryAdapter, private val countryApi: CountryApi, private val countryRepo: CountryRepo) : BaseViewModel<CountriesView>(), IAllCountriesViewModel {

    private val compositeDisposable = CompositeDisposable()

    override fun attachView(view: CountriesView, savedInstanceState: Bundle?) {
        super.attachView(view, savedInstanceState)

        compositeDisposable.add(
                countryRepo.favoriteChangeObservable
                        .subscribe({ adapter.notifyDataSetChanged() }, { Timber.e(it) })
        )
    }

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    override fun reloadData() {
        compositeDisposable.add(countryApi.getAllCountries()
                .doOnSuccess({ Collections.sort(it) })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    adapter.countryList = it
                    adapter.notifyDataSetChanged()
                    view?.onRefresh(true)
                }, {
                    Timber.e(it, "Could not load countries")
                    view?.onRefresh(false)
                })
        )
    }
}
