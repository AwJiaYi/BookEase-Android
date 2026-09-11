package com.jiayi.bookease.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jiayi.bookease.model.User

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun register(
        name: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->

                val firebaseUser = result.user

                if (firebaseUser == null) {
                    onError("Unable to create user.")
                    return@addOnSuccessListener
                }

                val user = User(
                    uid = firebaseUser.uid,
                    name = name,
                    email = email,
                    role = "customer"
                )

                firestore
                    .collection("users")
                    .document(firebaseUser.uid)
                    .set(user)
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener { exception ->
                        onError(
                            exception.message
                                ?: "Failed to save user information."
                        )
                    }
            }
            .addOnFailureListener { exception ->
                onError(
                    exception.message
                        ?: "Registration failed."
                )
            }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(
                    exception.message
                        ?: "Login failed."
                )
            }
    }

    fun logout() {
        auth.signOut()
    }

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUserProfile(
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    ) {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            onError("User not logged in.")
            return
        }

        firestore
            .collection("users")
            .document(currentUser.uid)
            .get()
            .addOnSuccessListener { document ->

                val user =
                    document.toObject(User::class.java)

                if (user != null) {
                    onSuccess(user)
                } else {
                    onError("User profile not found.")
                }
            }
            .addOnFailureListener { exception ->

                onError(
                    exception.message
                        ?: "Failed to load user profile."
                )
            }
    }
}