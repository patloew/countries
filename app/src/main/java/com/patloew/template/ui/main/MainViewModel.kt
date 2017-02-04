package com.patloew.template.ui.main

import com.patloew.template.injection.scopes.PerActivity
import com.patloew.template.ui.base.viewmodel.BaseViewModel

import javax.inject.Inject

@PerActivity
class MainViewModel
@Inject
constructor() : BaseViewModel<MainMvvm.View>(), MainMvvm.ViewModel
