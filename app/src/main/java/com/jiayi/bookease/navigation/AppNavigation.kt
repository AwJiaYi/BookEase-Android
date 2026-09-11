package com.jiayi.bookease.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jiayi.bookease.data.AuthRepository
import com.jiayi.bookease.ui.AddServiceScreen
import com.jiayi.bookease.ui.AdminServiceScreen
import com.jiayi.bookease.ui.BookingScreen
import com.jiayi.bookease.ui.EditServiceScreen
import com.jiayi.bookease.ui.HomeScreen
import com.jiayi.bookease.ui.LoginScreen
import com.jiayi.bookease.ui.MyBookingsScreen
import com.jiayi.bookease.ui.RegisterScreen
import com.jiayi.bookease.ui.ServiceDetailScreen
import com.jiayi.bookease.ui.ServiceListScreen

@Composable
fun AppNavigation() {

    val navController =
        rememberNavController()

    val authRepository =
        remember {
            AuthRepository()
        }

    var isAdmin by remember {
        mutableStateOf(false)
    }

    val startDestination =
        if (authRepository.isLoggedIn()) {
            "home"
        } else {
            "login"
        }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // =========================
        // LOGIN
        // =========================

        composable("login") {

            LoginScreen(

                onRegisterClick = {

                    navController.navigate(
                        "register"
                    )
                },

                onLoginSuccess = {

                    navController.navigate(
                        "home"
                    ) {

                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // =========================
        // REGISTER
        // =========================

        composable("register") {

            RegisterScreen(

                onBackToLogin = {

                    navController.popBackStack()
                },

                onRegisterSuccess = {

                    navController.navigate(
                        "home"
                    ) {

                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // =========================
        // HOME
        // =========================

        composable("home") {

            LaunchedEffect(Unit) {

                authRepository.getCurrentUserProfile(

                    onSuccess = { user ->

                        isAdmin =
                            user.role == "admin"
                    },

                    onError = {

                        isAdmin = false
                    }
                )
            }

            HomeScreen(

                onBrowseServices = {

                    navController.navigate(
                        "services"
                    )
                },

                onMyBookings = {

                    navController.navigate(
                        "myBookings"
                    )
                },

                onLogout = {

                    authRepository.logout()

                    isAdmin = false

                    navController.navigate(
                        "login"
                    ) {

                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                },

                onAdminPanel = {

                    if (isAdmin) {

                        navController.navigate(
                            "adminServices"
                        )
                    }
                },

                isAdmin = isAdmin
            )
        }

        // =========================
        // SERVICE LIST
        // =========================

        composable("services") {

            ServiceListScreen(

                onBack = {

                    navController.popBackStack()
                },

                onServiceClick = { service ->

                    navController.navigate(
                        "serviceDetail/${service.id}"
                    )
                }
            )
        }

        // =========================
        // SERVICE DETAIL
        // =========================

        composable(
            route =
                "serviceDetail/{serviceId}",

            arguments = listOf(

                navArgument(
                    "serviceId"
                ) {

                    type =
                        NavType.StringType
                }
            )
        ) { backStackEntry ->

            val serviceId =
                backStackEntry.arguments
                    ?.getString(
                        "serviceId"
                    )
                    ?: return@composable

            ServiceDetailScreen(

                serviceId =
                    serviceId,

                onBack = {

                    navController.popBackStack()
                },

                onBookAppointment = {
                        selectedServiceId ->

                    navController.navigate(
                        "booking/$selectedServiceId"
                    )
                }
            )
        }

        // =========================
        // BOOKING
        // =========================

        composable(
            route =
                "booking/{serviceId}",

            arguments = listOf(

                navArgument(
                    "serviceId"
                ) {

                    type =
                        NavType.StringType
                }
            )
        ) { backStackEntry ->

            val serviceId =
                backStackEntry.arguments
                    ?.getString(
                        "serviceId"
                    )
                    ?: return@composable

            BookingScreen(

                serviceId =
                    serviceId,

                onBack = {

                    navController.popBackStack()
                },

                onBookingSuccess = {

                    navController.navigate(
                        "home"
                    ) {

                        popUpTo("home") {
                            inclusive = false
                        }
                    }
                }
            )
        }

        // =========================
        // MY BOOKINGS
        // =========================

        composable(
            "myBookings"
        ) {

            MyBookingsScreen(

                onBack = {

                    navController.popBackStack()
                }
            )
        }

        // =========================
        // ADMIN SERVICE LIST
        // =========================

        composable(
            "adminServices"
        ) {

            if (!isAdmin) {

                LaunchedEffect(Unit) {

                    navController.navigate(
                        "home"
                    ) {

                        popUpTo(
                            "adminServices"
                        ) {

                            inclusive = true
                        }
                    }
                }

                return@composable
            }

            AdminServiceScreen(

                onBack = {

                    navController.popBackStack()
                },

                onAddService = {

                    navController.navigate(
                        "addService"
                    )
                },

                onEditService = {
                        serviceId ->

                    navController.navigate(
                        "editService/$serviceId"
                    )
                }
            )
        }

        // =========================
        // ADD SERVICE
        // =========================

        composable(
            "addService"
        ) {

            if (!isAdmin) {

                LaunchedEffect(Unit) {

                    navController.navigate(
                        "home"
                    ) {

                        popUpTo(
                            "addService"
                        ) {
                            inclusive = true
                        }
                    }
                }

                return@composable
            }

            AddServiceScreen(

                onBack = {

                    navController.popBackStack()
                },

                onServiceAdded = {

                    navController.popBackStack()
                }
            )
        }

        // =========================
        // EDIT SERVICE
        // =========================

        composable(
            route =
                "editService/{serviceId}",

            arguments = listOf(

                navArgument(
                    "serviceId"
                ) {

                    type =
                        NavType.StringType
                }
            )
        ) { backStackEntry ->

            if (!isAdmin) {

                LaunchedEffect(Unit) {

                    navController.navigate(
                        "home"
                    ) {

                        popUpTo(
                            "editService/{serviceId}"
                        ) {
                            inclusive = true
                        }
                    }
                }

                return@composable
            }

            val serviceId =
                backStackEntry.arguments
                    ?.getString(
                        "serviceId"
                    )
                    ?: return@composable

            EditServiceScreen(

                serviceId =
                    serviceId,

                onBack = {

                    navController.popBackStack()
                },

                onServiceUpdated = {

                    navController.popBackStack()
                }
            )
        }
    }
}