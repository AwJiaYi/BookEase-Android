package com.jiayi.bookease.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jiayi.bookease.data.ServiceRepository
import com.jiayi.bookease.model.Service

@Composable
fun ServiceListScreen(
    onBack: () -> Unit,
    onServiceClick: (Service) -> Unit
) {

    var services by remember {
        mutableStateOf<List<Service>>(emptyList())
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

    LaunchedEffect(Unit) {

        repository.getActiveServices(

            onSuccess = { result ->

                services = result
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

            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 16.dp
                )
            ) {

                TextButton(
                    onClick = onBack
                ) {
                    Text("← Back")
                }

                Text(
                    text = "Services",
                    style =
                        MaterialTheme.typography.headlineMedium
                )
            }
        }

    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            when {

                isLoading -> {

                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(24.dp)
                    )
                }

                errorMessage.isNotEmpty() -> {

                    Text(
                        text = errorMessage,
                        color =
                            MaterialTheme.colorScheme.error,
                        modifier =
                            Modifier.padding(24.dp)
                    )
                }

                services.isEmpty() -> {

                    Text(
                        text = "No services available.",
                        modifier =
                            Modifier.padding(24.dp)
                    )
                }

                else -> {

                    LazyColumn(
                        modifier =
                            Modifier.fillMaxSize(),

                        contentPadding =
                            PaddingValues(16.dp),

                        verticalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {

                        items(services) { service ->

                            ServiceCard(
                                service = service,
                                onClick = {
                                    onServiceClick(service)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceCard(
    service: Service,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {

        Column(
            modifier =
                Modifier.padding(18.dp)
        ) {

            Text(
                text = service.name,
                style =
                    MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )

            Text(
                text = service.description,
                style =
                    MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Text(
                text =
                    "${service.duration} minutes"
            )

            Text(
                text =
                    "RM %.2f".format(service.price)
            )
        }
    }
}