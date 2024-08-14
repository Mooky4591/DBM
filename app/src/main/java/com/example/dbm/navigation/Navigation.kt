package com.example.dbm.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.dbm.R
import com.example.dbm.account_settings.presentation.AccountSettingEvent
import com.example.dbm.account_settings.presentation.AccountSettingsScreen
import com.example.dbm.account_settings.presentation.AccountSettingsViewModel
import com.example.dbm.job.presentation.JobEvents
import com.example.dbm.job.presentation.JobScreen
import com.example.dbm.job.presentation.JobViewModel
import com.example.dbm.login.presentation.LoginEvents
import com.example.dbm.login.presentation.LoginScreen
import com.example.dbm.login.presentation.LoginViewModel
import com.example.dbm.main.presentation.MainEvents
import com.example.dbm.main.presentation.MainScreen
import com.example.dbm.main.presentation.MainViewModel
import com.example.dbm.presentation.EditTextEvent
import com.example.dbm.presentation.EditTextScreen
import com.example.dbm.presentation.EditTextViewModel
import com.example.dbm.register.presentation.RegisterEvents
import com.example.dbm.register.presentation.RegisterScreen
import com.example.dbm.register.presentation.RegisterViewModel

private const val s = "Change Password"

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
                        else -> {}
                    }
                }
                MainScreen(
                    state = state,
                    onEvent = { event ->
                        when (event) {
                            is MainEvents.OnSearchSelected -> navController.navigate(Screen.Search)
                            is MainEvents.OnFormsHistorySelected -> navController.navigate(Screen.JobsHistory(event.userId))
                            is MainEvents.UnsubmittedFormSelected -> navController.navigate(Screen.EditJob(event.formId))
                            is MainEvents.StartNewProject -> navController.navigate(Screen.Job)
                            is MainEvents.OnBackPress -> navController.navigateUp()
                            is MainEvents.OnAccountSettingsPressed -> navController.navigate(Screen.AccountSettings)
                            is MainEvents.OnContactUsPressed -> navController.navigate(Screen.ContactUs)
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
                            is JobEvents.OnBackPress -> navController.navigateUp()
                        }
                        jobViewModel.onEvent(event)
                    }
                )
            }
            //Account Settings
            composable<Screen.AccountSettings> {
                val settingsViewModel = hiltViewModel<AccountSettingsViewModel>()
                val state = settingsViewModel.state
                val context = LocalContext.current
                AccountSettingsScreen(
                    state = state,
                    onEvent = { event ->
                        when (event) {
                          is AccountSettingEvent.OnChangePasswordClicked -> {navController.navigate(Screen.EditText(null,
                              context.getString(
                                  R.string.change_password
                              )
                            )
                          )
                          }
                          is AccountSettingEvent.OnCompanyNameChangeClicked -> {navController.navigate(Screen.EditText(event.companyName, context.getString(R.string.change_company_name)))}
                          is AccountSettingEvent.OnEmailChangeClicked -> {navController.navigate(Screen.EditText(event.email, context.getString(R.string.change_email)))}
                          is AccountSettingEvent.OnCompanyAddressChangeClicked -> {navController.navigate(Screen.EditText(event.companyAddress, context.getString(R.string.change_company_address)))}
                          is AccountSettingEvent.OnPhoneNumberChangeClicked -> {navController.navigate(Screen.EditText(event.number, context.getString(R.string.change_phone_number)))}
                          is AccountSettingEvent.OnNameChangeClicked -> {navController.navigate(Screen.EditText(event.name, context.getString(R.string.change_name)))}
                        }
                        settingsViewModel.onEvent(event)
                    }
                    )

                }
            //EditTextView
            composable<Screen.EditText> {
                val editTextViewModel = hiltViewModel<EditTextViewModel>()
                val state = editTextViewModel.state
                val context = LocalContext.current
                val args = it.toRoute<Screen.EditText>()
                LaunchedEffect(args) {
                    editTextViewModel.setTitleText(args.title)
                    editTextViewModel.setText(args.text ?: "")
                }
                EditTextScreen(
                    state = state,
                    onEvent = { event ->
                        when (event){
                            EditTextEvent.OnBackPress -> navController.navigateUp()
                            EditTextEvent.OnSavePressed -> TODO()
                            is EditTextEvent.OnTextChanged -> editTextViewModel.onEvent(event)
                        }
                        editTextViewModel.onEvent(event)
                    }
                )
            }
            //Contact Us
            composable<Screen.ContactUs> {

            }
    }
}



