package com.patloew.countries.ui.base.navigator;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/* Copyright 2016 Patrick Löwenstein
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */
public abstract class BaseNavigator implements Navigator {

    abstract FragmentActivity getActivity();
    abstract FragmentManager getChildFragmentManager();

    @Override
    public final void startActivity(@NonNull Intent intent) {
        getActivity().startActivity(intent);
    }

    @Override
    public final void startActivity(@NonNull String action) {
        getActivity().startActivity(new Intent(action));
    }

    @Override
    public final void startActivity(@NonNull String action, @NonNull Uri uri) {
        getActivity().startActivity(new Intent(action, uri));
    }

    @Override
    public final void startActivity(@NonNull Class<? extends Activity> activityClass) {
        startActivity(activityClass, null);
    }

    @Override
    public final void startActivity(@NonNull Class<? extends Activity> activityClass, Bundle args) {
        Activity activity = getActivity();
        Intent intent = new Intent(activity, activityClass);
        if(args != null) { intent.putExtra(EXTRA_ARGS, args); }
        activity.startActivity(intent);
    }

    @Override
    public final void startActivity(@NonNull Class<? extends Activity> activityClass, Parcelable args) {
        Activity activity = getActivity();
        Intent intent = new Intent(activity, activityClass);
        if(args != null) { intent.putExtra(EXTRA_ARGS, args); }
        activity.startActivity(intent);
    }

    @Override
    public final void replaceFragment(@IdRes int containerId, Fragment fragment, Bundle args) {
        replaceFragmentInternal(getActivity().getSupportFragmentManager(), containerId, fragment, args, false, null);
    }

    @Override
    public final void replaceFragmentAndAddToBackstack(@IdRes int containerId, Fragment fragment, Bundle args, String backstackTag) {
        replaceFragmentInternal(getActivity().getSupportFragmentManager(), containerId, fragment, args, true, backstackTag);
    }

    @Override
    public void replaceChildFragment(@IdRes int containerId, @NonNull Fragment fragment, Bundle args) {
        replaceFragmentInternal(getChildFragmentManager(), containerId, fragment, args, false, null);
    }

    @Override
    public void replaceChildFragmentAndAddToBackstack(@IdRes int containerId, @NonNull Fragment fragment, Bundle args, String backstackTag) {
        replaceFragmentInternal(getChildFragmentManager(), containerId, fragment, args, true, backstackTag);
    }

    private void replaceFragmentInternal(FragmentManager fm, @IdRes int containerId, Fragment fragment, Bundle args, boolean addToBackstack,  String backstackTag) {
        FragmentTransaction ft = fm.beginTransaction().replace(containerId, fragment);
        if(addToBackstack) {
            ft.addToBackStack(backstackTag).commitNow();
        } else {
            ft.commit();
            fm.executePendingTransactions();
        }
    }
}
