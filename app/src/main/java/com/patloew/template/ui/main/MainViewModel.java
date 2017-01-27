package com.patloew.template.ui.main;

import com.patloew.template.injection.scopes.PerActivity;
import com.patloew.template.ui.base.viewmodel.BaseViewModel;

import javax.inject.Inject;

@PerActivity
public class MainViewModel extends BaseViewModel<MainMvvm.View> implements MainMvvm.ViewModel {

    @Inject
    public MainViewModel() { }

}
