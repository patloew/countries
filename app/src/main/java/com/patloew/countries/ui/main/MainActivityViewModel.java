package com.patloew.countries.ui.main;

import com.patloew.countries.ui.base.ViewModel;

public interface MainActivityViewModel extends ViewModel<MainActivityView> {

    void onRefresh(boolean initialLoading);

}
