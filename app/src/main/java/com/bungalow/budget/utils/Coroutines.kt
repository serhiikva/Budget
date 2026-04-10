package com.bungalow.budget.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun CoroutineScope.launchOnIoDispatcher(block: suspend CoroutineScope.() -> Unit): Job {
    return launch(Dispatchers.IO) { block.invoke(this) }
}