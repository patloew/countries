# Countries

[![Build Status](https://travis-ci.org/patloew/countries.svg?branch=master)](https://travis-ci.org/patloew/countries)

A sample Android app, which lists all countries with some additional information (currencies, languages, …). The app uses the MVVM pattern with the [Android data binding lib](http://developer.android.com/tools/data-binding/guide.html). Countries can be bookmarked and are then stored locally with [Realm](https://github.com/realm/realm-java). [Retrofit](https://github.com/square/retrofit) is used to fetch the country information from the free [REST Countries](http://restcountries.eu) service. For JSON parsing, custom [Gson](https://github.com/google/gson) TypeAdapters are used. [Parceler](https://github.com/johncarl81/parceler) is used to make the Country objects Parcelable. Also, [Dagger 2](https://github.com/google/dagger) is used for dependency injection.

The purpose of this is app is to show how:
* the MVVM pattern can be used with the data binding lib and a RecyclerView.
* [Retrofit, Realm, Parceler and Gson with custom TypeAdapters work together](https://nullpointer.wtf/android/using-retrofit-realm-parceler/)
* Dagger 2 can be used with different Scopes

This project can also be used as a template for new apps. Check out the template branch for a cleaned up version of this project.

# License

	Copyright 2016 Patrick Löwenstein

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	    http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.