package com.rickandmorty.extentions

import android.view.View

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
