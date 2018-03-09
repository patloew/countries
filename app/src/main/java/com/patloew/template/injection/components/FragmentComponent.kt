package com.patloew.template.injection.components

import com.patloew.template.injection.modules.FragmentModule
import com.patloew.template.injection.modules.ViewModelModule
import com.patloew.template.injection.scopes.PerFragment

import dagger.Component

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
 * FILE MODIFIED 2017 Tailored Media GmbH */
@PerFragment
@Component(dependencies = arrayOf(ActivityComponent::class), modules = arrayOf(FragmentModule::class, ViewModelModule::class))
interface FragmentComponent : FragmentComponentProvides {
    // create inject methods for your Fragments here

}

interface FragmentComponentProvides : ActivityComponentProvides {

}
