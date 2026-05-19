package com.example.krishisangam.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.krishisangam.buyer.BuyerDashboardScreen
import com.example.krishisangam.farmer.FarmerDashboardScreen
import com.example.krishisangam.manager.ManagerDashboardScreen
import com.example.krishisangam.transportation.TransportationDashboardScreen

object AuthRoutes {
    const val ROLE = "role"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val FORGOT = "forgot"
    const val VERIFY_EMAIL = "verify_email"

    const val FARMER_HOME = "farmer_home"
    const val BUYER_HOME = "buyer_home"
    const val MANAGER_HOME = "manager_home"
    const val TRANSPORT_HOME = "transport_home"
}

@Composable
fun AuthNavGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = AuthRoutes.ROLE
    ) {
        composable(AuthRoutes.ROLE) {
            RoleSelectionScreen(
                onBuyerClick = {
                    authViewModel.selectRole(UserRole.BUYER)
                    navController.navigate(AuthRoutes.LOGIN)
                },
                onFarmerClick = {
                    authViewModel.selectRole(UserRole.FARMER)
                    navController.navigate(AuthRoutes.LOGIN)
                },
                onManagerClick = {
                    authViewModel.selectRole(UserRole.NODE_MANAGER)
                    navController.navigate(AuthRoutes.LOGIN)
                },
                onTransportClick = {
                    navController.navigate(AuthRoutes.TRANSPORT_HOME) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(AuthRoutes.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navigateToDashboard(
                        navController = navController,
                        selectedRole = authViewModel.uiState.selectedRole
                    )
                },
                onGoToSignup = {
                    navController.navigate(AuthRoutes.SIGNUP)
                },
                onForgotPassword = {
                    navController.navigate(AuthRoutes.FORGOT)
                }
            )
        }

        composable(AuthRoutes.SIGNUP) {
            SignupScreen(
                viewModel = authViewModel,
                onSignupSuccess = {
                    navController.navigate(AuthRoutes.VERIFY_EMAIL)
                },
                onGoToLogin = {
                    navController.navigate(AuthRoutes.LOGIN) {
                        popUpTo(AuthRoutes.SIGNUP) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(AuthRoutes.FORGOT) {
            ForgotPasswordScreen(
                viewModel = authViewModel,
                onBackToLogin = {
                    navController.navigate(AuthRoutes.LOGIN) {
                        popUpTo(AuthRoutes.FORGOT) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(AuthRoutes.VERIFY_EMAIL) {
            EmailVerificationScreen(
                viewModel = authViewModel,
                onContinueToLogin = {
                    navController.navigate(AuthRoutes.LOGIN) {
                        popUpTo(AuthRoutes.VERIFY_EMAIL) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(AuthRoutes.BUYER_HOME) {
            BuyerDashboardScreen()
        }

        composable(AuthRoutes.FARMER_HOME) {
            FarmerDashboardScreen()
        }

        composable(AuthRoutes.MANAGER_HOME) {
            ManagerDashboardScreen()
        }

        composable(AuthRoutes.TRANSPORT_HOME) {
            TransportationDashboardScreen()
        }
    }
}

private fun navigateToDashboard(
    navController: NavController,
    selectedRole: UserRole?
) {
    when (selectedRole) {
        UserRole.BUYER -> {
            navController.navigate(AuthRoutes.BUYER_HOME) {
                popUpTo(AuthRoutes.ROLE) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }

        UserRole.FARMER -> {
            navController.navigate(AuthRoutes.FARMER_HOME) {
                popUpTo(AuthRoutes.ROLE) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }

        UserRole.NODE_MANAGER -> {
            navController.navigate(AuthRoutes.MANAGER_HOME) {
                popUpTo(AuthRoutes.ROLE) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }

        null -> {
            navController.navigate(AuthRoutes.ROLE) {
                launchSingleTop = true
            }
        }
    }
}

@Composable
fun PlaceholderHome(
    title: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title)
    }
}