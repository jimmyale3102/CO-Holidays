package dev.alejo.colombiaholidays.core.extensions

import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make

fun Context.snack(view: View, @StringRes message: Int) {
    make(view, message, Snackbar.LENGTH_LONG).show()
}