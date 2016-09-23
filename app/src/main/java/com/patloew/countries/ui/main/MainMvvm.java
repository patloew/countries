package com.patloew.countries.ui.main;

import com.patloew.countries.ui.base.view.MvvmView;
import com.patloew.countries.ui.base.viewmodel.MvvmViewModel;

public interface MainMvvm {

    interface View extends MvvmView {

    }

    interface ViewModel extends MvvmViewModel<View> {

    }
}
