package com.jiayi.bookease.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jiayi.bookease.model.Appointment
import com.jiayi.bookease.model.Service

class AppointmentRepository {

    private val firestore =
        FirebaseFirestore.getInstance()

    private val auth =
        FirebaseAuth.getInstance()

    fun createAppointment(
        service: Service,
        date: String,
        time: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        val currentUser =
            auth.currentUser

        if (currentUser == null) {
            onError("User not logged in.")
            return
        }

        // STEP 1:
        // Check whether this service/date/time
        // already has a confirmed booking.
        firestore
            .collection("appointments")
            .whereEqualTo(
                "serviceId",
                service.id
            )
            .whereEqualTo(
                "date",
                date
            )
            .whereEqualTo(
                "time",
                time
            )
            .whereEqualTo(
                "status",
                "confirmed"
            )
            .get()
            .addOnSuccessListener { snapshot ->

                // Slot already occupied
                if (!snapshot.isEmpty) {

                    onError(
                        "This time slot is already booked. " +
                                "Please choose another time."
                    )

                    return@addOnSuccessListener
                }

                // STEP 2:
                // Slot is available, create booking.

                val document =
                    firestore
                        .collection("appointments")
                        .document()

                val appointment =
                    Appointment(
                        id = document.id,
                        userId = currentUser.uid,
                        serviceId = service.id,
                        serviceName = service.name,
                        date = date,
                        time = time,
                        price = service.price,
                        status = "confirmed",
                        createdAt =
                            System.currentTimeMillis()
                    )

                document
                    .set(appointment)
                    .addOnSuccessListener {

                        onSuccess()
                    }
                    .addOnFailureListener { exception ->

                        onError(
                            exception.message
                                ?: "Failed to create appointment."
                        )
                    }
            }
            .addOnFailureListener { exception ->

                onError(
                    exception.message
                        ?: "Unable to check time slot availability."
                )
            }
    }

    fun getUserAppointments(
        onSuccess: (List<Appointment>) -> Unit,
        onError: (String) -> Unit
    ) {

        val currentUser =
            auth.currentUser

        if (currentUser == null) {

            onError(
                "User not logged in."
            )

            return
        }

        firestore
            .collection("appointments")
            .whereEqualTo(
                "userId",
                currentUser.uid
            )
            .get()
            .addOnSuccessListener { snapshot ->

                val appointments =
                    snapshot.documents
                        .mapNotNull { document ->

                            document
                                .toObject(
                                    Appointment::class.java
                                )
                                ?.copy(
                                    id = document.id
                                )
                        }
                        .sortedByDescending {
                            it.createdAt
                        }

                onSuccess(
                    appointments
                )
            }
            .addOnFailureListener { exception ->

                onError(
                    exception.message
                        ?: "Failed to load appointments."
                )
            }

    }
    fun cancelAppointment(
        appointmentId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        firestore
            .collection("appointments")
            .document(appointmentId)
            .update(
                "status",
                "cancelled"
            )
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->

                onError(
                    exception.message
                        ?: "Failed to cancel appointment."
                )
            }
    }

}