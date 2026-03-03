package com.investigate.data.utils

import android.util.Base64

fun String.toBase64(): String {
    return Base64.encodeToString(
        this.toByteArray(Charsets.UTF_8),
        Base64.URL_SAFE or Base64.NO_WRAP
    )
}

fun String.fromBase64(): String {
    val bytes = Base64.decode(this, Base64.URL_SAFE)
    return String(bytes, Charsets.UTF_8)
}