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
import com.example.dbm.presentation.edit_text.enum.EditTextType
import com.example.dbm.presentation.edit_text.presentation.EditTextEvent
import com.example.dbm.presentation.edit_text.presentation.EditTextScreen
import com.example.dbm.presentation.edit_text.presentation.EditTextViewModel
import com.example.dbm.register.presentation.RegisterEvents
import com.example.dbm.register.presentation.RegisterScreen
import com.example.dbm.register.presentation.RegisterViewModel

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
                            "Registration Failed: " + event.errorMessage.asString(context),
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
                        is MainEvents.OnLogoutPressed -> navController.navigate(Screen.Login)
                        else -> {}
                    }
                }
                MainScreen(
                    state = state,
                    onEvent = { event ->
                        when (event) {
                            is MainEvents.OnSearchSelected -> navController.navigate(Screen.Search)
                            is MainEvents.OnFormsHistorySelected -> navController.navigate(Screen.JobsHistory(event.userId))
                            is MainEvents.StartNewProject -> navController.navigate(Screen.Job(""))
                            is MainEvents.OnBackPress -> navController.navigateUp()
                            is MainEvents.OnAccountSettingsPressed -> navController.navigate(Screen.AccountSettings)
                            is MainEvents.OnContactUsPressed -> navController.navigate(Screen.ContactUs)
                            is MainEvents.OnUnfinishedJobSelected -> navController.navigate(Screen.Job(event.formId))
                            else -> { mainViewModel.onEvent(event) }
                        }
                    }
                )
            }
            //Job
            composable<Screen.Job> {
                val jobViewModel = hiltViewModel<JobViewModel>()
                val state = jobViewModel.state
                val questionStateList = jobViewModel.questionStates
                val otherQuestionState = jobViewModel.otherQuestionState
                val scopeOfWorkCheckBoxStateList = jobViewModel.scopeOfWorkCheckBoxStates
                val singleLineCheckBoxStateList = jobViewModel.singlePageCheckBoxState
                val siteInfoStateList = jobViewModel.siteInfoQuestionStateList
                val sitePhotoStateList = jobViewModel.sitePhotoStateList
                val context = LocalContext.current
                val args = it.toRoute<Screen.Job>()
                LaunchedEffect(args) {
                   jobViewModel.setJobId(args.jobId)
                }

                ObserveAsEvents(jobViewModel.events) { event ->
                    when (event) {
                        is JobEvents.OnSaveSuccessful -> {
                            Toast.makeText(context, "Save Successful", Toast.LENGTH_SHORT).show()
                            navController.navigateUp()
                        }
                        is JobEvents.OnSaveFailed -> Toast.makeText(context, event.failureMessage.asString(context), Toast.LENGTH_SHORT).show()
                        is JobEvents.OnBackPress -> navController.navigateUp()
                    }
                }
                JobScreen(
                    state = state,
                    onEvents = { event ->
                        jobViewModel.onEvent(event)
                    },
                    otherQuestionState = otherQuestionState,
                    scopeOfWorkCheckBoxState = scopeOfWorkCheckBoxStateList,
                    singleLineCheckBoxState = singleLineCheckBoxStateList,
                    siteInfoStateList = siteInfoStateList,
                    questionStateList = questionStateList,
                    sitePhotoStateList = sitePhotoStateList
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
                          is AccountSettingEvent.OnChangePasswordClicked -> {navController.navigate(Screen.EditText(null, context.getString(R.string.change_password)))}
                          is AccountSettingEvent.OnCompanyNameChangeClicked -> {navController.navigate(Screen.EditText(event.companyName, EditTextType.CHANGE_COMPANY_NAME.name))}
                          is AccountSettingEvent.OnEmailChangeClicked -> {navController.navigate(Screen.EditText(event.email, EditTextType.CHANGE_EMAIL.name))}
                          is AccountSettingEvent.OnCompanyAddressChangeClicked -> {navController.navigate(Screen.EditText(event.companyAddress, EditTextType.CHANGE_COMPANY_ADDRESS.name))}
                          is AccountSettingEvent.OnPhoneNumberChangeClicked -> {navController.navigate(Screen.EditText(event.number, EditTextType.CHANGE_PHONE_NUMBER.name))}
                          is AccountSettingEvent.OnNameChangeClicked -> {navController.navigate(Screen.EditText(event.name, EditTextType.CHANGE_NAME.name))}
                          is AccountSettingEvent.OnBackPress -> navController.navigate(Screen.Main)
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
                    editTextViewModel.setTitleText(args.title, context)
                    editTextViewModel.setText(args.text ?: "")
                }
                ObserveAsEvents(editTextViewModel.event) { event ->
                    when (event) {
                        is EditTextEvent.SaveSuccessful -> navController.navigate(Screen.AccountSettings)
                        is EditTextEvent.InvalidEntry -> Toast.makeText(
                            context,
                            "Invalid Entry: " + event.errorMessage.asString(context),
                            Toast.LENGTH_SHORT
                        ).show()
                        else -> {}
                    }
                }
                EditTextScreen(
                    state = state,
                    onEvent = { event ->
                        when (event){
                            EditTextEvent.OnBackPress -> navController.navigateUp()
                            else -> editTextViewModel.onEvent(event)
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



