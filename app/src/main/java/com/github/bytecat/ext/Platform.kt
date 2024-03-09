package com.github.bytecat.ext

import com.github.bytecat.Platform
import com.github.bytecat.R

val Platform.iconRes: Int get() {
    return when(this) {
        Platform.Android -> R.drawable.ic_android
        Platform.Mac -> R.drawable.ic_apple_finder
        Platform.IPhone -> R.drawable.ic_apple
        Platform.PC -> R.drawable.ic_microsoft_windows
        else -> R.drawable.ic_cat
    }
}