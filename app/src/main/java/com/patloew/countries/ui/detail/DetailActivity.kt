package com.patloew.countries.ui.detail

import android.databinding.Observable
import android.os.Bundle
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.Menu
import android.view.MenuItem
import com.patloew.countries.BR
import com.patloew.countries.R
import com.patloew.countries.databinding.ActivityDetailBinding
import com.patloew.countries.ui.base.BaseActivity
import com.patloew.countries.ui.base.navigator.Navigator

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
class DetailActivity : BaseActivity<ActivityDetailBinding, DetailMvvm.View, DetailMvvm.ViewModel>(), DetailMvvm.View {

    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        setAndBindContentView(savedInstanceState, R.layout.activity_detail)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setTitle(R.string.toolbar_title_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.update(intent.getParcelableExtra(Navigator.EXTRA_ARG), false)

        viewModel.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(observable: Observable, propertyId: Int) {
                if (propertyId == BR.bookmarkDrawable && menu != null) {
                    val favoriteItem = menu!!.findItem(R.id.menu_item_favorite)
                    favoriteItem.icon = viewModel.bookmarkDrawable
                    tintMenuIcon(favoriteItem)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        this.menu = menu
        val favoriteItem = menu.findItem(R.id.menu_item_favorite)
        val mapItem = menu.findItem(R.id.menu_item_maps)
        favoriteItem.icon = viewModel.bookmarkDrawable
        tintMenuIcon(favoriteItem)
        tintMenuIcon(mapItem)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { finish() }
            R.id.menu_item_favorite -> { viewModel.onBookmarkClick(null) }
            R.id.menu_item_maps -> { viewModel.onMapClick(null) }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun tintMenuIcon(menuItem: MenuItem) {
        val favoriteIcon = DrawableCompat.wrap(menuItem.icon.mutate())
        DrawableCompat.setTint(favoriteIcon, 0xFFFFFFFF.toInt())
        menuItem.icon = favoriteIcon
    }
}
