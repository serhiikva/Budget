package com.bungalow.budget.ui.screen.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bungalow.budget.ui.theme.medium

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoggedIn: () -> Unit
) {
    val context = LocalContext.current
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle(false)
    val loginErrorMessage by viewModel.loginErrorMessage.collectAsStateWithLifecycle(null)

    Content(
        onLoginClick = viewModel::login
    )

    loginErrorMessage?.let {
        Toast.makeText(
            context,
            it,
            Toast.LENGTH_LONG
        ).show()
    }

    if (isLoggedIn) onLoggedIn()
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    onLoginClick: (Context) -> Unit
) {
    val context = LocalContext.current
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Button(
            onClick = { onLoginClick(context) },
            modifier = Modifier
                .wrapContentSize()
                .padding(medium)
        ) {
            Text(
                text = "Sign In"
            )
        }
    }
}
