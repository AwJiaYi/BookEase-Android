package com.jiayi.bookease.data

import com.google.firebase.firestore.FirebaseFirestore
import com.jiayi.bookease.model.Service

class ServiceRepository {

    private val firestore =
        FirebaseFirestore.getInstance()

    fun getActiveServices(
        onSuccess: (List<Service>) -> Unit,
        onError: (String) -> Unit
    ) {

        firestore
            .collection("services")
            .whereEqualTo("active", true)
            .get()
            .addOnSuccessListener { snapshot ->

                val services =
                    snapshot.documents.mapNotNull { document ->

                        document
                            .toObject(Service::class.java)
                            ?.copy(
                                id = document.id
                            )
                    }

                onSuccess(services)
            }
            .addOnFailureListener { exception ->

                onError(
                    exception.message
                        ?: "Failed to load services."
                )
            }
    }

    fun getServiceById(
        serviceId: String,
        onSuccess: (Service) -> Unit,
        onError: (String) -> Unit
    ) {

        firestore
            .collection("services")
            .document(serviceId)
            .get()
            .addOnSuccessListener { document ->

                if (!document.exists()) {

                    onError("Service not found.")
                    return@addOnSuccessListener
                }

                val service =
                    document
                        .toObject(Service::class.java)
                        ?.copy(
                            id = document.id
                        )

                if (service != null) {

                    onSuccess(service)

                } else {

                    onError(
                        "Unable to load service."
                    )
                }
            }
            .addOnFailureListener { exception ->

                onError(
                    exception.message
                        ?: "Failed to load service."
                )
            }


    }

    fun addService(
        service: Service,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val document =
            firestore
                .collection("services")
                .document()

        val serviceWithId =
            service.copy(
                id = document.id
            )

        document
            .set(serviceWithId)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->

                onError(
                    exception.message
                        ?: "Failed to add service."
                )
            }
    }

    fun updateService(
        service: Service,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        firestore
            .collection("services")
            .document(service.id)
            .set(service)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->

                onError(
                    exception.message
                        ?: "Failed to update service."
                )
            }
    }

    fun setServiceActive(
        serviceId: String,
        active: Boolean,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        firestore
            .collection("services")
            .document(serviceId)
            .update(
                "active",
                active
            )
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->

                onError(
                    exception.message
                        ?: "Failed to update service status."
                )
            }
    }

    fun getAllServices(
        onSuccess: (List<Service>) -> Unit,
        onError: (String) -> Unit
    ) {

        firestore
            .collection("services")
            .get()
            .addOnSuccessListener { snapshot ->

                val services =
                    snapshot.documents.mapNotNull { document ->

                        document
                            .toObject(Service::class.java)
                            ?.copy(
                                id = document.id
                            )
                    }

                onSuccess(services)
            }
            .addOnFailureListener { exception ->

                onError(
                    exception.message
                        ?: "Failed to load services."
                )
            }
    }
}