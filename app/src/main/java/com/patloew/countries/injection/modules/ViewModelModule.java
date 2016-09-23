package com.patloew.countries.injection.modules;

import com.patloew.countries.ui.main.MainMvvm;
import com.patloew.countries.ui.main.MainViewModel;

import dagger.Binds;
import dagger.Module;

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
public abstract class ViewModelModule {
    // Bind your ViewModel implementations to interfaces here


    // Activities

    @Binds
    abstract MainMvvm.ViewModel bindMainViewModel(MainViewModel viewModel);


    // Fragments


    // View Holders


}
