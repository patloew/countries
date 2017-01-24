package com.tailoredapps.template.ui.main;

import com.tailoredapps.template.injection.scopes.PerActivity;
import com.tailoredapps.template.ui.base.viewmodel.BaseViewModel;

import javax.inject.Inject;

@PerActivity
public class MainViewModel extends BaseViewModel<MainMvvm.View> implements MainMvvm.ViewModel {

    @Inject
    public MainViewModel() { }

}
