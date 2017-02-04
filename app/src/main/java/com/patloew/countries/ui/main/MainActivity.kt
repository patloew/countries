package com.patloew.countries.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.LibsBuilder
import com.patloew.countries.R
import com.patloew.countries.databinding.ActivityMainBinding
import com.patloew.countries.ui.base.BaseActivity
import com.patloew.countries.ui.base.view.MvvmView
import com.patloew.countries.ui.base.viewmodel.NoOpViewModel
import com.patloew.countries.ui.main.viewpager.MainAdapter

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
 * limitations under the License.
 *
 * FILE MODIFIED 2017 Tailored Media GmbH */
 class MainActivity : BaseActivity<ActivityMainBinding, MvvmView, NoOpViewModel<MvvmView>>(), MvvmView {

    @Inject lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityComponent.inject(this)
        setAndBindContentView(savedInstanceState, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)

        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_item_licenses) {
            LibsBuilder()
                    .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                    .withActivityTitle(getString(R.string.menu_item_licenses))
                    .withLibraries("rxJavaAndroid", "parceler", "recyclerview_fastscroll", "gradle_retrolambda")
                    .withLicenseShown(true)
                    .start(this)
        }

        return super.onOptionsItemSelected(item)
    }

}
