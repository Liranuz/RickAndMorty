package com.task.utils

import android.app.Service
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.DrawableRes

import com.squareup.picasso.Picasso
import com.task.App
import com.task.R

fun View.visible(isVisible: Boolean, nonVisibleMode: Int) {
    this.visibility =  if (isVisible) View.VISIBLE else nonVisibleMode
}

fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toGone() {
    this.visibility = View.GONE
}

fun View.toInvisible() {
    this.visibility = View.GONE
}

fun ImageView.loadImage(@DrawableRes resId: Int) = Picasso.get().load(resId).into(this)
fun ImageView.loadImage(url: String) = Picasso.get().load(url).placeholder(R.drawable.ic_healthy_food).error(R.drawable.ic_healthy_food).into(this)
