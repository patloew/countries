package com.patloew.template.ui.main;

import com.patloew.template.ui.base.view.MvvmView;
import com.patloew.template.ui.base.viewmodel.MvvmViewModel;

public interface MainMvvm {

    interface View extends MvvmView {

    }

    interface ViewModel extends MvvmViewModel<View> {

    }
}
