package com.jiayi.bookease.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jiayi.bookease.data.ServiceRepository
import com.jiayi.bookease.model.Service

@Composable
fun EditServiceScreen(
    serviceId: String,
    onBack: () -> Unit,
    onServiceUpdated: () -> Unit
) {

    val repository = remember {
        ServiceRepository()
    }

    var service by remember {
        mutableStateOf<Service?>(null)
    }

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

    var active by remember {
        mutableStateOf(true)
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var isSaving by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    LaunchedEffect(serviceId) {

        repository.getServiceById(
            serviceId = serviceId,

            onSuccess = { result ->

                service = result

                name = result.name
                description = result.description
                duration = result.duration.toString()
                price = result.price.toString()
                active = result.active

                isLoading = false
            },

            onError = { message ->

                errorMessage = message
                isLoading = false
            }
        )
    }

    Scaffold(

        topBar = {

            TextButton(
                onClick = onBack
            ) {
                Text("← Back")
            }
        }

    ) { innerPadding ->

        if (isLoading) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                CircularProgressIndicator()
            }

            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {

            Text(
                text = "Edit Service",
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
                modifier =
                    Modifier.fillMaxWidth()
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
                modifier =
                    Modifier.fillMaxWidth()
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
                modifier =
                    Modifier.fillMaxWidth()
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
                modifier =
                    Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Text(
                    text =
                        if (active) {
                            "Service Active"
                        } else {
                            "Service Disabled"
                        }
                )

                Switch(
                    checked = active,
                    onCheckedChange = {
                        active = it
                    }
                )
            }

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

                    val originalService =
                        service
                            ?: return@Button

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

                            val updatedService =
                                originalService.copy(
                                    name =
                                        name.trim(),

                                    description =
                                        description.trim(),

                                    duration =
                                        durationValue,

                                    price =
                                        priceValue,

                                    active =
                                        active
                                )

                            repository.updateService(
                                service =
                                    updatedService,

                                onSuccess = {

                                    isSaving = false

                                    onServiceUpdated()
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

                    CircularProgressIndicator(
                        modifier =
                            Modifier.size(20.dp)
                    )

                } else {

                    Text("Save Changes")
                }
            }
        }
    }
}