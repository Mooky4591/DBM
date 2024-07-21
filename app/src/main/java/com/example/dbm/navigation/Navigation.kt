package com.example.dbm.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.dbm.login.presentation.LoginEvents
import com.example.dbm.login.presentation.LoginScreen
import com.example.dbm.login.presentation.LoginViewModel
import com.example.dbm.register.presentation.RegisterEvents
import com.example.dbm.register.presentation.RegisterScreen
import com.example.dbm.register.presentation.RegisterViewModel

@Composable
fun Nav() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login_screen") {
        navigation(
            startDestination = Screen.Login.route,
            route = "login_screen"
        ) {
            //Login Screen
            composable(route = Screen.Login.route) {
                val loginViewModel = hiltViewModel<LoginViewModel>()
                val state = loginViewModel.state
                val context = LocalContext.current
                ObserveAsEvents(loginViewModel.events) { event ->
                    when (event) {
                        is LoginEvents.LoginSuccess -> {

                        }
                        is LoginEvents.LoginFailed -> {

                        }
                        else -> {}
                    }
                }
                LoginScreen(
                    state = state,
                    onEvent = { event ->
                        when (event) {
                            is LoginEvents.OnRegisterLinkClick -> navController.navigate(Screen.Register.route)
                            else -> {}
                        }
                        loginViewModel.onEvent(event)
                    },
                )
            }

            //Register Screen
            composable(route = Screen.Register.route) {
                val registerViewModel = hiltViewModel<RegisterViewModel>()
                val state = registerViewModel.state
                val context = LocalContext.current
                ObserveAsEvents(registerViewModel.event) { event ->
                    when (event) {
                        is RegisterEvents.RegistrationSuccessful -> TODO()
                        is RegisterEvents.RegistrationFailed -> TODO()
                    }
                }
                RegisterScreen(
                    state = state,
                    onEvent = { registerViewModel.onEvent(it) }
                )
            }
        }
    }
}
