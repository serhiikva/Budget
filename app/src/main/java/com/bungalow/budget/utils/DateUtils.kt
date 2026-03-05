package com.bungalow.budget.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Long.toLocalDateTime(): String {

    val formatter = DateTimeFormatter.ofPattern("dd MMM HH:mm")

    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}