package com.jiayi.bookease.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onBrowseServices: () -> Unit,
    onMyBookings: () -> Unit,
    onLogout: () -> Unit,
    onAdminPanel: () -> Unit,
    isAdmin: Boolean
) {

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
            modifier =
                Modifier.height(8.dp)
        )

        Text(
            text = "Appointment made simple",
            style =
                MaterialTheme.typography.bodyLarge
        )

        Spacer(
            modifier =
                Modifier.height(32.dp)
        )

        Card(
            modifier =
                Modifier.fillMaxWidth()
        ) {

            Column(
                modifier =
                    Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Book a Service",
                    style =
                        MaterialTheme.typography.titleLarge
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(
                    text =
                        "Browse available services and choose your preferred appointment time."
                )

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                Button(
                    onClick =
                        onBrowseServices,
                    modifier =
                        Modifier.fillMaxWidth()
                ) {
                    Text("Browse Services")
                }
            }
        }

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        Card(
            modifier =
                Modifier.fillMaxWidth()
        ) {

            Column(
                modifier =
                    Modifier.padding(20.dp)
            ) {

                Text(
                    text = "My Appointments",
                    style =
                        MaterialTheme.typography.titleLarge
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(
                    text =
                        "View your confirmed and cancelled bookings."
                )

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                OutlinedButton(
                    onClick =
                        onMyBookings,
                    modifier =
                        Modifier.fillMaxWidth()
                ) {
                    Text("My Bookings")
                }
            }
        }

        if (isAdmin) {

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            Card(
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier =
                        Modifier.padding(20.dp)
                ) {

                    Text(
                        text = "Administration",
                        style =
                            MaterialTheme.typography.titleLarge
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    Text(
                        text =
                            "Manage available services and business information."
                    )

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )

                    Button(
                        onClick =
                            onAdminPanel,
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {
                        Text("Admin Panel")
                    }
                }
            }
        }

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )

        TextButton(
            onClick =
                onLogout
        ) {
            Text("Logout")
        }
    }
}