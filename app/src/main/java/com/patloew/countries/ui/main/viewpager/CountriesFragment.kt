package com.patloew.countries.ui.main.viewpager

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.patloew.countries.R
import com.patloew.countries.databinding.FragmentRecyclerviewBinding
import com.patloew.countries.ui.base.BaseFragment
import com.patloew.countries.ui.base.viewmodel.MvvmViewModel
import com.patloew.countries.ui.main.recyclerview.CountryAdapter

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
 * FILE MODIFIED 2017 Tailored Media GmbH*/
abstract class CountriesFragment<V : MvvmViewModel<CountriesView>> : BaseFragment<FragmentRecyclerviewBinding, V>(), CountriesView {

    @Inject protected lateinit var adapter: CountryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return setAndBindContentView(inflater, container, savedInstanceState, R.layout.fragment_recyclerview)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    // View

    override fun onRefresh(success: Boolean) {
        binding.swipeRefreshLayout.isRefreshing = false
    }

}
