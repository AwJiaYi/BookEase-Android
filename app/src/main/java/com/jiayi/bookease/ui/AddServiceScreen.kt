package com.jiayi.bookease.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jiayi.bookease.data.ServiceRepository
import com.jiayi.bookease.model.Service

@Composable
fun AddServiceScreen(
    onBack: () -> Unit,
    onServiceAdded: () -> Unit
) {

    var name by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var duration by remember {
        mutableStateOf("")
    }

    var price by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var isSaving by remember {
        mutableStateOf(false)
    }

    val repository = remember {
        ServiceRepository()
    }

    Scaffold(

        topBar = {

            TextButton(
                onClick = onBack,
                modifier = Modifier.padding(
                    start = 8.dp,
                    top = 8.dp
                )
            ) {
                Text("← Back")
            }
        }

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {

            Text(
                text = "Add Service",
                style =
                    MaterialTheme.typography.headlineMedium
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
                    Text("Service Name")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                },
                label = {
                    Text("Description")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            OutlinedTextField(
                value = duration,
                onValueChange = {
                    duration = it
                },
                label = {
                    Text("Duration (minutes)")
                },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Number
                    ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            OutlinedTextField(
                value = price,
                onValueChange = {
                    price = it
                },
                label = {
                    Text("Price (RM)")
                },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Decimal
                    ),
                modifier = Modifier.fillMaxWidth()
            )

            if (
                errorMessage.isNotEmpty()
            ) {

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
                modifier = Modifier.height(24.dp)
            )

            Button(
                onClick = {

                    errorMessage = ""

                    val durationValue =
                        duration.toLongOrNull()

                    val priceValue =
                        price.toDoubleOrNull()

                    when {

                        name.isBlank() -> {
                            errorMessage =
                                "Please enter a service name."
                        }

                        description.isBlank() -> {
                            errorMessage =
                                "Please enter a description."
                        }

                        durationValue == null ||
                                durationValue <= 0 -> {

                            errorMessage =
                                "Please enter a valid duration."
                        }

                        priceValue == null ||
                                priceValue < 0 -> {

                            errorMessage =
                                "Please enter a valid price."
                        }

                        else -> {

                            isSaving = true

                            val service =
                                Service(
                                    name =
                                        name.trim(),
                                    description =
                                        description.trim(),
                                    duration =
                                        durationValue,
                                    price =
                                        priceValue,
                                    active = true
                                )

                            repository.addService(
                                service = service,

                                onSuccess = {

                                    isSaving = false

                                    onServiceAdded()
                                },

                                onError = { message ->

                                    isSaving = false

                                    errorMessage =
                                        message
                                }
                            )
                        }
                    }
                },

                enabled = !isSaving,

                modifier =
                    Modifier.fillMaxWidth()
            ) {

                if (isSaving) {

                    CircularProgressIndicator()

                } else {

                    Text("Save Service")
                }
            }
        }
    }
}