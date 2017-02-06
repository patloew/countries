package com.tailoredapps.template.ui.main

import com.tailoredapps.template.ui.base.view.MvvmView
import com.tailoredapps.template.ui.base.viewmodel.MvvmViewModel

interface MainMvvm {

    interface View : MvvmView

    interface ViewModel : MvvmViewModel<View>
}
