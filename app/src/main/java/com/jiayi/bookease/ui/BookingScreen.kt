package com.jiayi.bookease.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jiayi.bookease.data.AppointmentRepository
import com.jiayi.bookease.data.ServiceRepository
import com.jiayi.bookease.model.Service
import java.util.Calendar

@Composable
fun BookingScreen(
    serviceId: String,
    onBack: () -> Unit,
    onBookingSuccess: () -> Unit
) {

    var service by remember {
        mutableStateOf<Service?>(null)
    }

    var selectedDate by remember {
        mutableStateOf("")
    }

    var selectedTime by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var isSubmitting by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    val context =
        LocalContext.current

    val serviceRepository =
        remember {
            ServiceRepository()
        }

    val appointmentRepository =
        remember {
            AppointmentRepository()
        }

    val timeOptions = listOf(
        "09:00 AM",
        "10:00 AM",
        "11:00 AM",
        "01:00 PM",
        "02:00 PM",
        "03:00 PM",
        "04:00 PM"
    )

    var timeMenuExpanded by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(serviceId) {

        serviceRepository.getServiceById(
            serviceId = serviceId,

            onSuccess = {
                service = it
                isLoading = false
            },

            onError = {
                errorMessage = it
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

        if (isLoading) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp)
            ) {
                CircularProgressIndicator()
            }

            return@Scaffold
        }

        val currentService =
            service ?: return@Scaffold

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {

            Text(
                text = "Book Appointment",
                style =
                    MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )

            Text(
                text = currentService.name,
                style =
                    MaterialTheme.typography.titleLarge
            )

            Text(
                text =
                    "RM %.2f".format(
                        currentService.price
                    )
            )

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )

            Button(
                onClick = {

                    val calendar =
                        Calendar.getInstance()

                    DatePickerDialog(
                        context,
                        { _, year, month, day ->

                            selectedDate =
                                "%02d/%02d/%04d".format(
                                    day,
                                    month + 1,
                                    year
                                )
                        },
                        calendar.get(
                            Calendar.YEAR
                        ),
                        calendar.get(
                            Calendar.MONTH
                        ),
                        calendar.get(
                            Calendar.DAY_OF_MONTH
                        )
                    ).show()
                },

                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    if (
                        selectedDate.isEmpty()
                    ) {
                        "Select Date"
                    } else {
                        selectedDate
                    }
                )
            }

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            Box {

                OutlinedButton(
                    onClick = {
                        timeMenuExpanded = true
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text(
                        if (
                            selectedTime.isEmpty()
                        ) {
                            "Select Time"
                        } else {
                            selectedTime
                        }
                    )
                }

                DropdownMenu(
                    expanded =
                        timeMenuExpanded,

                    onDismissRequest = {
                        timeMenuExpanded = false
                    }
                ) {

                    timeOptions.forEach { time ->

                        DropdownMenuItem(
                            text = {
                                Text(time)
                            },

                            onClick = {

                                selectedTime = time

                                timeMenuExpanded =
                                    false
                            }
                        )
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )

            if (
                selectedDate.isNotEmpty() &&
                selectedTime.isNotEmpty()
            ) {

                HorizontalDivider()

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                Text(
                    text = "Booking Summary",
                    style =
                        MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(
                    "Service: ${currentService.name}"
                )

                Text(
                    "Date: $selectedDate"
                )

                Text(
                    "Time: $selectedTime"
                )

                Text(
                    "Price: RM %.2f".format(
                        currentService.price
                    )
                )
            }

            if (
                errorMessage.isNotEmpty()
            ) {

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                Text(
                    text = errorMessage,
                    color =
                        MaterialTheme.colorScheme.error
                )
            }

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )

            Button(
                onClick = {

                    errorMessage = ""

                    if (
                        selectedDate.isEmpty()
                    ) {

                        errorMessage =
                            "Please select a date."

                        return@Button
                    }

                    if (
                        selectedTime.isEmpty()
                    ) {

                        errorMessage =
                            "Please select a time."

                        return@Button
                    }

                    isSubmitting = true

                    appointmentRepository
                        .createAppointment(
                            service =
                                currentService,

                            date =
                                selectedDate,

                            time =
                                selectedTime,

                            onSuccess = {

                                isSubmitting =
                                    false

                                onBookingSuccess()
                            },

                            onError = {

                                isSubmitting =
                                    false

                                errorMessage = it
                            }
                        )
                },

                enabled =
                    !isSubmitting,

                modifier =
                    Modifier.fillMaxWidth()
            ) {

                if (
                    isSubmitting
                ) {

                    CircularProgressIndicator(
                        modifier =
                            Modifier.size(20.dp),
                        strokeWidth =
                            2.dp
                    )

                } else {

                    Text(
                        "Confirm Booking"
                    )
                }
            }
        }
    }
}