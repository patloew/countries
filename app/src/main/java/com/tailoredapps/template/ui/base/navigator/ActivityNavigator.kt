package com.tailoredapps.template.ui.base.navigator

import android.app.Activity
import android.app.DialogFragment
import android.content.Intent
import android.net.Uri
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

    override fun finishActivityWithResult(resultCode: Int, resultIntentFun: (Intent.() -> Unit)?) {
        val intent = resultIntentFun?.let { Intent().apply(it) }
        activity.setResult(resultCode, intent)
        activity.finish()
    }

    override fun finishAffinity() {
        activity.finishAffinity()
    }

    override fun startActivity(intent: Intent) {
        activity.startActivity(intent)
    }

    override fun startActivity(action: String, uri: Uri?) {
        activity.startActivity(Intent(action, uri))
    }

    override fun startActivity(activityClass: Class<out Activity>, adaptIntentFun: (Intent.() -> Unit)?) {
        startActivityInternal(activityClass, null, adaptIntentFun)
    }

    override fun startActivityForResult(activityClass: Class<out Activity>, requestCode: Int, adaptIntentFun: (Intent.() -> Unit)?) {
        startActivityInternal(activityClass, requestCode, adaptIntentFun)
    }

    private fun startActivityInternal(activityClass: Class<out Activity>, requestCode: Int?, adaptIntentFun: (Intent.() -> Unit)?) {
        val intent = Intent(activity, activityClass)
        adaptIntentFun?.invoke(intent)

        if (requestCode != null) {
            activity.startActivityForResult(intent, requestCode)
        } else {
            activity.startActivity(intent)
        }
    }

    override fun replaceFragment(@IdRes containerId: Int, fragment: Fragment, fragmentTag: String?) {
        replaceFragmentInternal(activity.supportFragmentManager, containerId, fragment, fragmentTag, false, null)
    }

    override fun replaceFragmentAndAddToBackStack(@IdRes containerId: Int, fragment: Fragment, fragmentTag: String?, backstackTag: String?) {
        replaceFragmentInternal(activity.supportFragmentManager, containerId, fragment, fragmentTag, true, backstackTag)
    }

    protected fun replaceFragmentInternal(fm: FragmentManager, @IdRes containerId: Int, fragment: Fragment, fragmentTag: String?, addToBackstack: Boolean, backstackTag: String?) {
        val ft = fm.beginTransaction().replace(containerId, fragment, fragmentTag)
        if (addToBackstack) {
            ft.addToBackStack(backstackTag).commit()
            fm.executePendingTransactions()
        } else {
            ft.commitNow()
        }
    }

    override fun <T : DialogFragment> showDialogFragment(dialog: T, fragmentTag: String) {
        dialog.show(activity.fragmentManager, fragmentTag)
    }

    override fun popFragmentBackStackImmediate() {
        activity.supportFragmentManager.popBackStackImmediate()
    }
}
