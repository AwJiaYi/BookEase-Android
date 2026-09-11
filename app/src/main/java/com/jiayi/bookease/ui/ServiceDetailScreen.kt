package com.jiayi.bookease.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jiayi.bookease.data.ServiceRepository
import com.jiayi.bookease.model.Service

@Composable
fun ServiceDetailScreen(
    serviceId: String,
    onBack: () -> Unit,
    onBookAppointment: (String) -> Unit
) {

    var service by remember {
        mutableStateOf<Service?>(null)
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    val repository = remember {
        ServiceRepository()
    }

    LaunchedEffect(serviceId) {

        repository.getServiceById(
            serviceId = serviceId,

            onSuccess = { result ->

                service = result
                errorMessage = ""
                isLoading = false
            },

            onError = { message ->

                service = null
                errorMessage = message
                isLoading = false
            }
        )
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

        when {

            isLoading -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            errorMessage.isNotEmpty() -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            service != null -> {

                val currentService = service!!

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp)
                ) {

                    Text(
                        text = currentService.name,
                        style =
                            MaterialTheme.typography.headlineLarge
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = currentService.description,
                        style =
                            MaterialTheme.typography.bodyLarge
                    )

                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )

                    HorizontalDivider()

                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = "Duration",
                            style =
                                MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text =
                                "${currentService.duration} minutes"
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = "Price",
                            style =
                                MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "RM %.2f".format(
                                currentService.price
                            )
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )

                    HorizontalDivider()

                    Spacer(
                        modifier = Modifier.height(32.dp)
                    )

                    if (currentService.active) {

                        Button(
                            onClick = {

                                onBookAppointment(
                                    currentService.id
                                )
                            },

                            modifier =
                                Modifier.fillMaxWidth()
                        ) {

                            Text("Book Appointment")
                        }

                    } else {

                        Text(
                            text =
                                "This service is currently unavailable.",
                            color =
                                MaterialTheme.colorScheme.error
                        )

                        Spacer(
                            modifier =
                                Modifier.height(12.dp)
                        )

                        Button(
                            onClick = {},
                            enabled = false,
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {

                            Text("Unavailable")
                        }
                    }
                }
            }

            else -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "Service not found."
                    )
                }
            }
        }
    }
}