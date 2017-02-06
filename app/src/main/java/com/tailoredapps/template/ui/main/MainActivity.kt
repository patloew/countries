package com.tailoredapps.template.ui.main

import android.os.Bundle
import com.tailoredapps.template.R
import com.tailoredapps.template.databinding.ActivityMainBinding
import com.tailoredapps.template.ui.base.BaseActivity

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
class MainActivity : BaseActivity<ActivityMainBinding, MainMvvm.View, MainMvvm.ViewModel>(), MainMvvm.View {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityComponent.inject(this)
        setAndBindContentView(savedInstanceState, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)
    }

}
