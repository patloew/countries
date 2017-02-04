package com.patloew.countries.ui.base.navigator

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager

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
 * limitations under the License.
 *
 *
 * ------
 *
 * FILE CHANGED 2017 Tailored Media GmbH
 *
 */
open class ActivityNavigator(protected val activity: FragmentActivity) : Navigator {

    override fun finishActivity() {
        activity.finish()
    }

    override fun startActivity(intent: Intent) {
        activity.startActivity(intent)
    }

    override fun startActivity(action: String) {
        activity.startActivity(Intent(action))
    }

    override fun startActivity(action: String, uri: Uri) {
        activity.startActivity(Intent(action, uri))
    }

    override fun startActivity(activityClass: Class<out Activity>) {
        startActivityInternal(activityClass, null, null)
    }

    override fun startActivity(activityClass: Class<out Activity>, setArgsAction: (Intent) -> Unit) {
        startActivityInternal(activityClass, setArgsAction, null)
    }

    override fun startActivity(activityClass: Class<out Activity>, args: Bundle) {
        startActivityInternal(activityClass, { intent -> intent.putExtra(Navigator.Companion.EXTRA_ARG, args) }, null)
    }

    override fun startActivity(activityClass: Class<out Activity>, arg: Parcelable) {
        startActivityInternal(activityClass, { intent -> intent.putExtra(Navigator.Companion.EXTRA_ARG, arg) }, null)
    }

    override fun startActivity(activityClass: Class<out Activity>, arg: String) {
        startActivityInternal(activityClass, { intent -> intent.putExtra(Navigator.Companion.EXTRA_ARG, arg) }, null)
    }

    override fun startActivity(activityClass: Class<out Activity>, arg: Int) {
        startActivityInternal(activityClass, { intent -> intent.putExtra(Navigator.Companion.EXTRA_ARG, arg) }, null)
    }

    override fun startActivityForResult(activityClass: Class<out Activity>, requestCode: Int) {
        startActivityInternal(activityClass, null, requestCode)
    }

    override fun startActivityForResult(activityClass: Class<out Activity>, setArgsAction: (Intent) -> Unit, requestCode: Int) {
        startActivityInternal(activityClass, setArgsAction, requestCode)
    }

    override fun startActivityForResult(activityClass: Class<out Activity>, arg: Parcelable, requestCode: Int) {
        startActivityInternal(activityClass, { intent -> intent.putExtra(Navigator.Companion.EXTRA_ARG, arg) }, requestCode)
    }

    override fun startActivityForResult(activityClass: Class<out Activity>, arg: String, requestCode: Int) {
        startActivityInternal(activityClass, { intent -> intent.putExtra(Navigator.Companion.EXTRA_ARG, arg) }, requestCode)
    }

    override fun startActivityForResult(activityClass: Class<out Activity>, arg: Int, requestCode: Int) {
        startActivityInternal(activityClass, { intent -> intent.putExtra(Navigator.Companion.EXTRA_ARG, arg) }, requestCode)
    }

    private fun startActivityInternal(activityClass: Class<out Activity>, setArgsAction: ((Intent) -> Unit)?, requestCode: Int?) {
        val intent = Intent(activity, activityClass)
        setArgsAction?.invoke(intent)

        if (requestCode != null) {
            activity.startActivityForResult(intent, requestCode)
        } else {
            activity.startActivity(intent)
        }
    }

    override fun replaceFragment(@IdRes containerId: Int, fragment: Fragment, args: Bundle) {
        replaceFragmentInternal(activity.supportFragmentManager, containerId, fragment, null, args, false, null)
    }

    override fun replaceFragment(@IdRes containerId: Int, fragment: Fragment, fragmentTag: String, args: Bundle) {
        replaceFragmentInternal(activity.supportFragmentManager, containerId, fragment, fragmentTag, args, false, null)
    }

    override fun replaceFragmentAndAddToBackStack(@IdRes containerId: Int, fragment: Fragment, args: Bundle, backstackTag: String) {
        replaceFragmentInternal(activity.supportFragmentManager, containerId, fragment, null, args, true, backstackTag)
    }

    override fun replaceFragmentAndAddToBackStack(@IdRes containerId: Int, fragment: Fragment, fragmentTag: String, args: Bundle, backstackTag: String) {
        replaceFragmentInternal(activity.supportFragmentManager, containerId, fragment, fragmentTag, args, true, backstackTag)
    }

    protected fun replaceFragmentInternal(fm: FragmentManager, @IdRes containerId: Int, fragment: Fragment, fragmentTag: String?, args: Bundle?, addToBackstack: Boolean, backstackTag: String?) {
        if (args != null) { fragment.arguments = args }
        val ft = fm.beginTransaction().replace(containerId, fragment, fragmentTag)
        if (addToBackstack) {
            ft.addToBackStack(backstackTag).commit()
            fm.executePendingTransactions()
        } else {
            ft.commitNow()
        }
    }
}
