package com.bungalow.budget.utils

fun String.toSentenceCase(): String {
    return this.lowercase().replaceFirstChar { it.uppercase() }
}