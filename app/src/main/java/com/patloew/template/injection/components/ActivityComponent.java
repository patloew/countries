package com.patloew.template.injection.components;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.patloew.template.injection.modules.ActivityModule;
import com.patloew.template.injection.modules.ViewModelModule;
import com.patloew.template.injection.qualifier.ActivityContext;
import com.patloew.template.injection.qualifier.ActivityFragmentManager;
import com.patloew.template.injection.scopes.PerActivity;
import com.patloew.template.ui.base.navigator.Navigator;
import com.patloew.template.ui.main.MainActivity;

import dagger.Component;

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
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, ViewModelModule.class})
public interface ActivityComponent extends AppComponent {
    
    @ActivityContext Context activityContext();
    @ActivityFragmentManager FragmentManager defaultFragmentManager();
    Navigator navigator();

    // create inject methods for your Activities here

    void inject(MainActivity activity);

}
