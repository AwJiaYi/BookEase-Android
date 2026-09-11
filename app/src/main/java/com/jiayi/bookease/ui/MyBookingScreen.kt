package com.jiayi.bookease.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.jiayi.bookease.data.AppointmentRepository
import com.jiayi.bookease.model.Appointment

@Composable
fun MyBookingsScreen(
    onBack: () -> Unit
) {

    var appointments by remember {
        mutableStateOf<List<Appointment>>(emptyList())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    val repository = remember {
        AppointmentRepository()
    }

    fun loadAppointments() {

        isLoading = true
        errorMessage = ""

        repository.getUserAppointments(

            onSuccess = { result ->
                appointments = result
                isLoading = false
            },

            onError = { message ->
                errorMessage = message
                isLoading = false
            }
        )
    }

    LaunchedEffect(Unit) {
        loadAppointments()
    }

    Scaffold(

        topBar = {

            Column {

                TextButton(
                    onClick = onBack,
                    modifier = Modifier.padding(
                        start = 8.dp,
                        top = 8.dp
                    )
                ) {
                    Text("← Back")
                }

                Text(
                    text = "My Bookings",
                    style =
                        MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(
                        start = 24.dp,
                        bottom = 12.dp
                    )
                )
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
                        color =
                            MaterialTheme.colorScheme.error
                    )
                }
            }

            appointments.isEmpty() -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text =
                            "You don't have any bookings yet."
                    )
                }
            }

            else -> {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),

                    contentPadding =
                        PaddingValues(16.dp),

                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    items(
                        appointments,
                        key = { appointment ->
                            appointment.id
                        }
                    ) { appointment ->

                        BookingCard(
                            appointment = appointment,

                            onCancel = {

                                repository.cancelAppointment(
                                    appointmentId =
                                        appointment.id,

                                    onSuccess = {
                                        loadAppointments()
                                    },

                                    onError = { message ->
                                        errorMessage = message
                                    }
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BookingCard(
    appointment: Appointment,
    onCancel: () -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        Column(
            modifier =
                Modifier.padding(18.dp)
        ) {

            Text(
                text =
                    appointment.serviceName,
                style =
                    MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            HorizontalDivider()

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Text("Date")

                Text(
                    appointment.date
                )
            }

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Text("Time")

                Text(
                    appointment.time
                )
            }

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Text("Price")

                Text(
                    "RM %.2f".format(
                        appointment.price
                    )
                )
            }

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Text("Status")

                Text(
                    appointment.status
                        .replaceFirstChar {
                            it.uppercase()
                        }
                )
            }

            if (
                appointment.status == "confirmed"
            ) {

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                Button(
                    onClick = onCancel,
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text("Cancel Booking")
                }
            }
        }
    }
}