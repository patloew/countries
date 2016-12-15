package com.patloew.countries.injection.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.patloew.countries.BuildConfig;
import com.patloew.countries.data.model.Country;
import com.patloew.countries.data.model.RealmString;
import com.patloew.countries.data.model.RealmStringMapEntry;
import com.patloew.countries.data.remote.CountryApi;
import com.patloew.countries.injection.scopes.PerApplication;
import com.patloew.countries.util.CountryTypeAdapter;
import com.patloew.countries.util.RealmStringListTypeAdapter;
import com.patloew.countries.util.RealmStringMapEntryListTypeAdapter;

import dagger.Module;
import dagger.Provides;
import io.realm.RealmList;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import io.reactivex.schedulers.Schedulers;

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
@Module
public class NetModule {

    @Provides
    @PerApplication
    static Gson provideGson() {
        return new GsonBuilder()
                // Custom type adapters for models are not needed when using Gson, but this
                // type adapter is a good example if you want to write one yourself.
                .registerTypeAdapter(Country.class, CountryTypeAdapter.INSTANCE)
                // These type adapters for RealmLists are needed, since RealmString and RealmStringMapEntry
                // wrappers are not recognized by Gson in the default configuration.
                .registerTypeAdapter(new TypeToken<RealmList<RealmString>>(){}.getType(), RealmStringListTypeAdapter.INSTANCE)
                .registerTypeAdapter(new TypeToken<RealmList<RealmStringMapEntry>>(){}.getType(), RealmStringMapEntryListTypeAdapter.INSTANCE)
                .create();
    }

    @Provides
    @PerApplication
    static OkHttpClient provideOkHttpClient() {
        return new OkHttpClient();
    }

    @Provides
    @PerApplication
    static CountryApi provideCountryApi(Gson gson, OkHttpClient okHttpClient) {
        OkHttpClient.Builder httpClientBuilder = okHttpClient.newBuilder();

        if(BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClientBuilder.addInterceptor(loggingInterceptor);
        }

        return new Retrofit.Builder()
                .baseUrl("https://restcountries.eu/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .callFactory(httpClientBuilder.build())
                .build().create(CountryApi.class);
    }
}
