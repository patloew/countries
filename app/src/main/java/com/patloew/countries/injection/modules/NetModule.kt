package com.patloew.countries.injection.modules

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.patloew.countries.BuildConfig
import com.patloew.countries.data.model.Country
import com.patloew.countries.data.model.RealmString
import com.patloew.countries.data.model.RealmStringMapEntry
import com.patloew.countries.data.remote.CountryApi
import com.patloew.countries.injection.scopes.PerApplication
import com.patloew.countries.util.CountryTypeAdapter
import com.patloew.countries.util.RealmStringListTypeAdapter
import com.patloew.countries.util.RealmStringMapEntryListTypeAdapter

import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import io.realm.RealmList
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
 * ------
 *
 * FILE MODIFIED 2017 Tailored Media GmbH
 * */
@Module
class NetModule() {

    @Provides
    @PerApplication
    internal fun provideGson(): Gson {
        return GsonBuilder()
                // Custom type adapters for models are not needed when using Gson, but this
                // type adapter is a good example if you want to write one yourself.
                .registerTypeAdapter(Country::class.java, CountryTypeAdapter.INSTANCE)
                // These type adapters for RealmLists are needed, since RealmString and RealmStringMapEntry
                // wrappers are not recognized by Gson in the default configuration.
                .registerTypeAdapter(object : TypeToken<RealmList<RealmString>>() {}.type, RealmStringListTypeAdapter.INSTANCE)
                .registerTypeAdapter(object : TypeToken<RealmList<RealmStringMapEntry>>() {}.type, RealmStringMapEntryListTypeAdapter.INSTANCE)
                .create()
    }

    @Provides
    @PerApplication
    internal fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient()
    }

    @Provides
    @PerApplication
    internal fun provideCountryApi(gson: Gson, okHttpClient: OkHttpClient): CountryApi {
        val httpClientBuilder = okHttpClient.newBuilder()

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClientBuilder.addInterceptor(loggingInterceptor)
        }

        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .callFactory(httpClientBuilder.build())
                .build().create(CountryApi::class.java)
    }
}
