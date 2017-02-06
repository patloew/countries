package com.tailoredapps.template.injection.modules

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

import com.tailoredapps.template.injection.qualifier.ChildFragmentManager
import com.tailoredapps.template.injection.scopes.PerFragment
import com.tailoredapps.template.ui.base.navigator.ChildFragmentNavigator
import com.tailoredapps.template.ui.base.navigator.FragmentNavigator

import dagger.Module
import dagger.Provides

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
class FragmentModule(private val fragment: Fragment) {

    @Provides
    @PerFragment
    @ChildFragmentManager
    internal fun provideChildFragmentManager(): FragmentManager {
        return fragment.childFragmentManager
    }

    @Provides
    @PerFragment
    internal fun provideFragmentNavigator(): FragmentNavigator {
        return ChildFragmentNavigator(fragment)
    }

}
