package com.bungalow.budget.ui.dialog.add_payment

import com.bungalow.budget.R

enum class Mode(val titleResId: Int) {
    AddPayment(R.string.dialog_add_payment_title_mode_add),
    EditPayment(R.string.dialog_add_payment_title_mode_edit)
}