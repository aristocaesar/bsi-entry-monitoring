package com.bsi.entrymonitoring.utils

import android.content.Context
import android.app.AlertDialog

object AlertDialogManager {

    fun showErrorDialog(
        context: Context,
        title: String? = null,
        message: String,
        positiveButtonText: String? = "OK",
        negativeButtonText: String? = null,
        onPositiveClick: (() -> Unit)? = null,
        onNegativeClick: (() -> Unit)? = null
    ) {
        val alertDialogManagerBuilder = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)

        title?.let {
            alertDialogManagerBuilder.setTitle(it)
        }

        positiveButtonText?.let {
            alertDialogManagerBuilder.setPositiveButton(it) { dialog, _ ->
                onPositiveClick?.invoke()
                dialog.dismiss()
            }
        }

        negativeButtonText?.let {
            alertDialogManagerBuilder.setNegativeButton(it) { dialog, _ ->
                onNegativeClick?.invoke()
                dialog.dismiss()
            }
        }

        val alertDialog = alertDialogManagerBuilder.create()

        alertDialog.show()
    }

}