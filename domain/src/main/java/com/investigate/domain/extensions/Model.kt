package com.investigate.domain.extensions

import com.investigate.domain.model.CategoryPayment

fun CategoryPayment.matches(search: String): Boolean {
    return amount.toString().contains(search, true) ||
            note.contains(search, true)
}