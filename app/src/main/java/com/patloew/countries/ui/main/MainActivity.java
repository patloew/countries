package com.patloew.countries.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.patloew.countries.R;
import com.patloew.countries.databinding.ActivityMainBinding;
import com.patloew.countries.ui.base.BaseActivity;
import com.patloew.countries.ui.base.view.MvvmView;
import com.patloew.countries.ui.base.viewmodel.NoOpViewModel;
import com.patloew.countries.ui.main.viewpager.CountriesMvvm;
import com.patloew.countries.ui.main.viewpager.MainAdapter;

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
 * limitations under the License. */
public class MainActivity extends BaseActivity<ActivityMainBinding, NoOpViewModel> implements MvvmView {

    @Inject MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityComponent().inject(this);
        setAndBindContentView(R.layout.activity_main, savedInstanceState);

        setSupportActionBar(binding.toolbar);

        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override public void onPageScrollStateChanged(int state) {}

            @Override public void onPageSelected(int position) {
                if (position == 0) {
                    Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + binding.viewPager.getCurrentItem());
                    ((CountriesMvvm.View) currentFragment).notifyDataSetChanged();
                }
            }
        });
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
