package com.tailoredapps.template.ui.main;

import com.tailoredapps.template.ui.base.view.MvvmView;
import com.tailoredapps.template.ui.base.viewmodel.MvvmViewModel;

public interface MainMvvm {

    interface View extends MvvmView {

    }

    interface ViewModel extends MvvmViewModel<View> {

    }
}
