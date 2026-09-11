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
fun RegisterScreen(
    onBackToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val authRepository = remember {
        AuthRepository()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
            },
            label = {
                Text("Name")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text("Email")
            },
            modifier = Modifier.fillMaxWidth()
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
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
            },
            label = {
                Text("Confirm Password")
            },
            visualTransformation =
                PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage.isNotEmpty()) {

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(
            onClick = {

                errorMessage = ""

                when {

                    name.isBlank() -> {
                        errorMessage =
                            "Please enter your name."
                    }

                    email.isBlank() -> {
                        errorMessage =
                            "Please enter your email."
                    }

                    password.length < 6 -> {
                        errorMessage =
                            "Password must contain at least 6 characters."
                    }

                    password != confirmPassword -> {
                        errorMessage =
                            "Passwords do not match."
                    }

                    else -> {

                        isLoading = true

                        authRepository.register(
                            name = name.trim(),
                            email = email.trim(),
                            password = password,

                            onSuccess = {
                                isLoading = false
                                onRegisterSuccess()
                            },

                            onError = { message ->
                                isLoading = false
                                errorMessage = message
                            }
                        )
                    }
                }
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

                Text("Register")
            }
        }

        TextButton(
            onClick = onBackToLogin,
            enabled = !isLoading
        ) {
            Text(
                "Already have an account? Login"
            )
        }
    }
}