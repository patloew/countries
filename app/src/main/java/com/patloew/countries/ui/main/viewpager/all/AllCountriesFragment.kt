package com.patloew.countries.ui.main.viewpager.all

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import com.patloew.countries.R
import com.patloew.countries.ui.main.viewpager.CountriesFragment
import com.patloew.countries.ui.main.viewpager.CountriesView

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
class AllCountriesFragment : CountriesFragment<IAllCountriesViewModel>(), CountriesView {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.reloadData() }
        if (savedInstanceState == null) { binding.swipeRefreshLayout.isRefreshing = true }
        viewModel.reloadData()
    }


    // View

    override fun onRefresh(success: Boolean) {
        super.onRefresh(success)

        if (!success) {
            Snackbar.make(binding.recyclerView, "Could not load countries", Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.snackbar_action_retry) { viewModel.reloadData() }
                    .show()
        }
    }
}
