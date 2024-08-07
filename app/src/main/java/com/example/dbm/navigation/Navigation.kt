package com.example.dbm.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.dbm.job.presentation.JobScreen
import com.example.dbm.job.presentation.JobViewModel
import com.example.dbm.login.presentation.LoginEvents
import com.example.dbm.login.presentation.LoginScreen
import com.example.dbm.login.presentation.LoginViewModel
import com.example.dbm.main.presentation.MainEvents
import com.example.dbm.main.presentation.MainScreen
import com.example.dbm.main.presentation.MainViewModel
import com.example.dbm.register.presentation.RegisterEvents
import com.example.dbm.register.presentation.RegisterScreen
import com.example.dbm.register.presentation.RegisterViewModel
import kotlinx.serialization.Serializable
import java.io.Serial

@Composable
fun Nav() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login) {
            //Login Screen
            composable<Screen.Login> {
                val loginViewModel = hiltViewModel<LoginViewModel>()
                val state = loginViewModel.state
                val context = LocalContext.current
                ObserveAsEvents(loginViewModel.events) { event ->
                    when (event) {
                        is LoginEvents.LoginSuccess -> navController.navigate(Screen.Main)

                        is LoginEvents.LoginFailed -> {
                            Toast.makeText(
                                context,
                                "Log in unsuccessful: " + state.loginErrorMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {}
                    }
                }
                LoginScreen(
                    state = state,
                    onEvent = { event ->
                        when (event) {
                            is LoginEvents.OnRegisterLinkClick -> navController.navigate(Screen.Register)
                            else -> {}
                        }
                        loginViewModel.onEvent(event)
                    },
                )
            }

            //Register Screen
            composable<Screen.Register> {
                val registerViewModel = hiltViewModel<RegisterViewModel>()
                val state = registerViewModel.state
                val context = LocalContext.current
                ObserveAsEvents(registerViewModel.event) { event ->
                    when (event) {
                        is RegisterEvents.RegistrationSuccessful -> navController.navigate(Screen.Login)

                        is RegisterEvents.RegistrationFailed -> Toast.makeText(
                            context,
                            "Registration Failed: " + event.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
                RegisterScreen(
                    state = state,
                    onEvent = { registerViewModel.onEvent(it) }
                )
            }
            //Main Screen
            composable<Screen.Main> {
                val mainViewModel = hiltViewModel<MainViewModel>()
                val state = mainViewModel.state
                val context = LocalContext.current
                ObserveAsEvents(mainViewModel.event) { event ->
                    when (event) {
                        is MainEvents.StartNewProject -> navController.navigate(Screen.Job)
                        else -> { mainViewModel.onEvent(event) }
                    }
                }
                MainScreen(
                    state = state,
                    onEvent = { event ->
                        when (event) {
                            is MainEvents.OnSearchSelected -> navController.navigate(Screen.Search)
                            is MainEvents.OnFormsHistorySelected -> navController.navigate(Screen.FormsHistory(event.userId))
                            is MainEvents.UnsubmittedFormSelected -> navController.navigate(Screen.EditForm(event.formId))
                            is MainEvents.StartNewProject -> navController.navigate(Screen.Job)
                            is MainEvents.OnBackPress -> navController.navigateUp()
                            else -> { mainViewModel.onEvent(event) }
                        }
                    }
                )
            }
            //Job
            composable<Screen.Job> {
                val jobViewModel = hiltViewModel<JobViewModel>()
                val state = jobViewModel.state
                val context = LocalContext.current
                JobScreen(
                    state = state,
                    onEvents = { event ->
                        when (event) {

                        }
                        jobViewModel.onEvent(event)
                    }
                )
            }
    }
}



