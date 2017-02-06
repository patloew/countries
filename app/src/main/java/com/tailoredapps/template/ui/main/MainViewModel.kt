package com.tailoredapps.template.ui.main

import com.tailoredapps.template.injection.scopes.PerActivity
import com.tailoredapps.template.ui.base.viewmodel.BaseViewModel

import javax.inject.Inject

@PerActivity
class MainViewModel
@Inject
constructor() : BaseViewModel<MainMvvm.View>(), MainMvvm.ViewModel
