package com.jiayi.bookease.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jiayi.bookease.data.ServiceRepository
import com.jiayi.bookease.model.Service

@Composable
fun AdminServiceScreen(
    onBack: () -> Unit,
    onAddService: () -> Unit,
    onEditService: (String) -> Unit
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

    fun loadServices() {

        isLoading = true
        errorMessage = ""

        repository.getAllServices(

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

    LaunchedEffect(Unit) {
        loadServices()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        TextButton(
            onClick = onBack
        ) {
            Text("← Back")
        }

        Text(
            text = "Admin Services",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(
            onClick = onAddService,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Service")
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        OutlinedButton(
            onClick = {
                loadServices()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Refresh")
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        when {

            isLoading -> {

                CircularProgressIndicator()
            }

            errorMessage.isNotEmpty() -> {

                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }

            services.isEmpty() -> {

                Text(
                    text = "No services found."
                )
            }

            else -> {

                LazyColumn(
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    items(
                        items = services,
                        key = { service ->
                            service.id
                        }
                    ) { service ->

                        Card(
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {

                            Column(
                                modifier =
                                    Modifier.padding(16.dp)
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
                                    text =
                                        "RM %.2f".format(
                                            service.price
                                        )
                                )

                                Text(
                                    text =
                                        "${service.duration} minutes"
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(6.dp)
                                )

                                Row {

                                    Text(
                                        text = "Status: "
                                    )

                                    Text(
                                        text =
                                            if (service.active) {
                                                "Active"
                                            } else {
                                                "Disabled"
                                            }
                                    )
                                }

                                Spacer(
                                    modifier =
                                        Modifier.height(12.dp)
                                )

                                Button(
                                    onClick = {
                                        onEditService(
                                            service.id
                                        )
                                    },
                                    modifier =
                                        Modifier.fillMaxWidth()
                                ) {
                                    Text("Edit")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}