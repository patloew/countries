package com.patloew.template.ui.main

import com.patloew.template.ui.base.view.MvvmView
import com.patloew.template.ui.base.viewmodel.MvvmViewModel

interface MainMvvm {

    interface View : MvvmView

    interface ViewModel : MvvmViewModel<View>
}
