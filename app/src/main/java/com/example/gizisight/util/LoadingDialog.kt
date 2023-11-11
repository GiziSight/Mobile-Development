package com.example.gizisight.util

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.example.gizisight.R

class LoadingDialog(private val activity: Activity) {
    private lateinit var alertDialog: AlertDialog

    fun startLoadingDialog() {
        val builder = AlertDialog.Builder(activity)

        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.loading_dialog, null))
        builder.setCancelable(true)

        alertDialog = builder.create()
        alertDialog.show()
    }

    fun dismiss() {
        alertDialog.dismiss()
    }
}