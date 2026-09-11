package com.jiayi.bookease.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.jiayi.bookease.data.AuthRepository

@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit,
    onLoginSuccess: () -> Unit
) {

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    val authRepository = remember {
        AuthRepository()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement =
            Arrangement.Center,

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text = "BookEase",
            style =
                MaterialTheme.typography.headlineLarge
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Appointment made simple",
            style =
                MaterialTheme.typography.bodyMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        OutlinedTextField(
            value = email,

            onValueChange = {
                email = it
            },

            label = {
                Text("Email")
            },

            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = password,

            onValueChange = {
                password = it
            },

            label = {
                Text("Password")
            },

            visualTransformation =
                PasswordVisualTransformation(),

            modifier =
                Modifier.fillMaxWidth()
        )

        if (errorMessage.isNotEmpty()) {

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = errorMessage,
                color =
                    MaterialTheme.colorScheme.error
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(
            onClick = {

                errorMessage = ""

                if (
                    email.isBlank() ||
                    password.isBlank()
                ) {

                    errorMessage =
                        "Please enter your email and password."

                    return@Button
                }

                isLoading = true

                authRepository.login(
                    email = email.trim(),
                    password = password,

                    onSuccess = {

                        isLoading = false

                        onLoginSuccess()
                    },

                    onError = { message ->

                        isLoading = false

                        errorMessage = message
                    }
                )
            },

            enabled = !isLoading,

            modifier =
                Modifier.fillMaxWidth()
        ) {

            if (isLoading) {

                CircularProgressIndicator(
                    modifier =
                        Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )

            } else {

                Text("Login")
            }
        }

        TextButton(
            onClick = onRegisterClick,
            enabled = !isLoading
        ) {

            Text(
                "Don't have an account? Register"
            )
        }
    }
}