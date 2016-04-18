package com.patloew.countries.dagger;

import android.app.Application;
import android.content.pm.PackageManager;

import com.patloew.countries.BuildConfig;
import com.patloew.countries.CountriesApp;
import com.patloew.countries.dagger.scopes.PerApplication;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

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
public class AppModule {

    @Provides
    @PerApplication
    static Application provideApplication() {
        return CountriesApp.getInstance();
    }

    @Provides
    @PerApplication
    @Named("mapsAvailable")
    static boolean provideMapsAvailable(Application app) {
        try {
            app.getPackageManager().getPackageInfo("com.google.android.apps.maps", 0);
            return true;
        } catch(PackageManager.NameNotFoundException ignore) {
            return false;
        }
    }

    @Provides
    @PerApplication
    static RealmConfiguration provideRealmConfiguration(Application app) {
        RealmConfiguration.Builder builder = new RealmConfiguration.Builder(app);
        if(BuildConfig.DEBUG) { builder = builder.deleteRealmIfMigrationNeeded(); }
        return builder.build();
    }

    @Provides
    static Realm provideRealm(RealmConfiguration realmConfiguration) {
        return Realm.getInstance(realmConfiguration);
    }

}
