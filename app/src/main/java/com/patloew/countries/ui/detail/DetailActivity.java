package com.patloew.countries.ui.detail;

import android.databinding.Observable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.Menu;
import android.view.MenuItem;

import com.patloew.countries.BR;
import com.patloew.countries.R;
import com.patloew.countries.databinding.ActivityDetailBinding;
import com.patloew.countries.ui.base.BaseActivity;
import com.patloew.countries.ui.base.navigator.Navigator;

import org.parceler.Parcels;

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
public class DetailActivity extends BaseActivity<ActivityDetailBinding, DetailMvvm.ViewModel> implements DetailMvvm.View {

    private Menu menu = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setAndBindContentView(R.layout.activity_detail, savedInstanceState);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(R.string.toolbar_title_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel.update(Parcels.unwrap(getIntent().getParcelableExtra(Navigator.EXTRA_ARGS)), false);

        viewModel.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int propertyId) {
                if(propertyId == BR.bookmarkDrawable && menu != null) {
                    MenuItem favoriteItem = menu.findItem(R.id.menu_item_favorite);
                    favoriteItem.setIcon(viewModel.getBookmarkDrawable());
                    tintMenuIcon(favoriteItem);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        this.menu = menu;
        MenuItem favoriteItem = menu.findItem(R.id.menu_item_favorite);
        MenuItem mapItem = menu.findItem(R.id.menu_item_maps);
        favoriteItem.setIcon(viewModel.getBookmarkDrawable());
        tintMenuIcon(favoriteItem);
        tintMenuIcon(mapItem);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
            case R.id.menu_item_favorite: {
                viewModel.onBookmarkClick(null);
                break;
            }
            case R.id.menu_item_maps: {
                viewModel.onMapClick(null);
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private static void tintMenuIcon(MenuItem menuItem) {
        Drawable favoriteIcon = DrawableCompat.wrap(menuItem.getIcon().mutate());
        DrawableCompat.setTint(favoriteIcon, 0xFFFFFFFF);
        menuItem.setIcon(favoriteIcon);
    }
}
