package com.tailoredapps.template.ui.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.tailoredapps.template.R;
import com.tailoredapps.template.databinding.ActivityMainBinding;
import com.tailoredapps.template.ui.base.BaseActivity;
import com.tailoredapps.template.ui.base.view.MvvmView;
import com.tailoredapps.template.ui.base.viewmodel.NoOpViewModel;
import com.tailoredapps.template.ui.main.viewpager.MainAdapter;

import javax.inject.Inject;

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
public class MainActivity extends BaseActivity<ActivityMainBinding, NoOpViewModel> implements MvvmView {

    @Inject MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityComponent().inject(this);
        setAndBindContentView(savedInstanceState, R.layout.activity_main);

        setSupportActionBar(binding.toolbar);

        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_item_licenses) {
            new LibsBuilder()
                    .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                    .withActivityTitle(getString(R.string.menu_item_licenses))
                    .withLibraries("rxJavaAndroid", "parceler", "recyclerview_fastscroll", "gradle_retrolambda")
                    .withLicenseShown(true)
                    .start(this);
        }

        return super.onOptionsItemSelected(item);
    }

}
