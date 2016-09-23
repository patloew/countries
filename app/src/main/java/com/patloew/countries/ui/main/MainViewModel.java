package com.patloew.countries.ui.main;

import com.patloew.countries.injection.scopes.PerActivity;
import com.patloew.countries.ui.base.viewmodel.BaseViewModel;

import javax.inject.Inject;

@PerActivity
public class MainViewModel extends BaseViewModel<MainMvvm.View> implements MainMvvm.ViewModel {

    @Inject
    public MainViewModel() { }

}
