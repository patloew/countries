package com.patloew.countries.view;

import com.patloew.countries.model.Country;

import java.util.List;

public interface MainActivityView {
    void onRefresh(boolean success, List<Country> countries);
}
