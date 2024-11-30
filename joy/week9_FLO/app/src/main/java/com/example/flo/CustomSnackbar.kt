package com.example.flo

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.example.flo.databinding.CustomSnackbarBinding
import com.google.android.material.snackbar.Snackbar

class CustomSnackbar(view: View, private val message: String) {

    companion object {
        fun make(view: View, message: String) = CustomSnackbar(view, message)
    }

    private val context = view.context
    private val snackbar = Snackbar.make(view, "", 2000)
    //private val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

    //private val inflater = LayoutInflater.from(context)
    private val snackbarBinding: CustomSnackbarBinding = CustomSnackbarBinding.inflate(LayoutInflater.from(context), null, false)

    init {
        initView()
    }

    private fun initView() {

        snackbar.view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        snackbarBinding.customSnackbarTv.text = message

        (snackbar.view as Snackbar.SnackbarLayout).apply {
            removeAllViews()
            setPadding(0, 0, 0, 0)
            addView(snackbarBinding.root)
        }

        snackbar.view.translationY = -310f
    }

    fun show() {
        snackbar.show()
    }
}