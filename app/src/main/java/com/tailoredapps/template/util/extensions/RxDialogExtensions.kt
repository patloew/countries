package com.tailoredapps.template.util.extensions

import android.app.AlertDialog
import android.content.Context
import com.tailoredapps.template.util.RxDialogException
import io.reactivex.Completable

/**
 * Created by Florian Schuster
 * florian.schuster@tailored-apps.com
 */

fun Context.rxDialog(title: String, text: String,
                     positiveId: Int = android.R.string.yes,
                     negativeId: Int? = android.R.string.cancel,
                     cancelable: Boolean = false
): Completable = rxDialogInternal(
        title, text,
        this.getString(positiveId), negativeId?.let { this.getString(it) },
        cancelable
)

fun Context.rxDialog(titleId: Int, textId: Int,
                     positiveId: Int = android.R.string.yes,
                     negativeId: Int? = android.R.string.cancel,
                     cancelable: Boolean = false
): Completable = rxDialogInternal(
        this.getString(titleId), this.getString(textId),
        this.getString(positiveId), negativeId?.let { this.getString(it) },
        cancelable
)

private fun Context.rxDialogInternal(title: String, text: String,
                                     positiveText: String, negativeText: String?,
                                     cancelable: Boolean
): Completable = Completable.create { emitter ->
    AlertDialog.Builder(this).apply {
        setTitle(title)
        setMessage(text)
        setPositiveButton(positiveText, { _, _ -> emitter.onComplete() })
        if (negativeText != null) setNegativeButton(negativeText, { _, _ -> emitter.onError(RxDialogException.negative()) })
        setCancelable(cancelable)
        if (cancelable) setOnCancelListener { emitter.onError(RxDialogException.canceled()) }
        show()
    }
}