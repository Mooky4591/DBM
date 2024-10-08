package com.example.dbm.register.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.dbm.R
import com.example.dbm.login.presentation.objects.User
import com.example.dbm.login.presentation.TopLogo
import com.example.dbm.presentation.theme.DbmPurple
import com.example.dbm.presentation.theme.GradientDarkPurple
import com.example.dbm.presentation.theme.GradientPink

@Composable
fun RegisterScreen(
    state: RegisterState,
    onEvent: (RegisterEvents) -> Unit
) {
    Scaffold {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black,
                            Color.Gray,
                            Color.LightGray
                        )
                    )
                )
                .padding(it.calculateLeftPadding(layoutDirection = LayoutDirection.Ltr))
        ) {
            TopLogo()
            Box (modifier = Modifier.fillMaxSize()) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    shape = AbsoluteRoundedCornerShape(60.dp, 60.dp, 900.dp, 0.dp)
                ) {}
                val scrollState = rememberScrollState()
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(30.dp)
                        .verticalScroll(scrollState)
                ) {
                    CreateRegistrationFields(onEvent = { value -> onEvent(value) }, state = state)
                }
            }
        }
    }
}

@Composable
fun CreateRegistrationFields(
    onEvent: (RegisterEvents) -> Unit,
    state: RegisterState
) {
    CreateTextField(
        hint = stringResource(R.string.first_name),
        onEvent = { firstname ->
            onEvent(RegisterEvents.OnFirstNameChanged(firstname))
        },
        stringValue = state.firstName ?: ""
    )
    Spacer(modifier = Modifier.height(10.dp))
    CreateTextField(
        hint = stringResource(R.string.last_name),
        onEvent = { lastName ->
            onEvent(RegisterEvents.OnLastNameChanged(lastName))
        },
        stringValue = state.lastName ?: "",
    )
    Spacer(modifier = Modifier.height(10.dp))
    CreateTextField(
        hint = stringResource(R.string.email),
        onEvent = { email ->
            onEvent(RegisterEvents.OnEmailChanged(email))
        },
        stringValue = state.email ?: "",
    )
    Spacer(modifier = Modifier.height(10.dp))
    CreateTextField(
        hint = stringResource(R.string.phone_number),
        onEvent = { phoneNumber ->
            onEvent(RegisterEvents.OnPhoneNumberChanged(phoneNumber))
        },
        stringValue = state.phoneNumber ?: "",
    )
    Spacer(modifier = Modifier.height(10.dp))
    CreateTextField(
        hint = stringResource(R.string.company_address),
        onEvent = { address ->
            onEvent(RegisterEvents.OnCompanyAddressChanged(address))
        },
        stringValue = state.companyAddress ?: ""
    )
    Spacer(modifier = Modifier.height(10.dp))
    CreateTextField(
        hint = stringResource(R.string.company_name),
        onEvent = { name ->
            onEvent(RegisterEvents.OnCompanyNameChanged(name))
        },
        stringValue = state.companyName ?: ""
    )
    Spacer(modifier = Modifier.height(10.dp))
    CreateTextField(
        hint = stringResource(id = R.string.password),
        onEvent = { password ->
            onEvent(RegisterEvents.OnPasswordChanged(password))
        },
        stringValue = state.password ?: "",
    )
    val registerObject = User(
        userId = null,
        email = state.email ?: "",
        firstName = state.firstName ?: "",
        lastName = state.lastName ?: "",
        phoneNumber = state.phoneNumber ?: "",
        companyAddress = state.companyAddress ?: "",
        companyName = state.companyName ?: ""
    )
    Spacer(modifier = Modifier.height(15.dp))
    RegisterButton(
        onClick = { user -> onEvent(RegisterEvents.OnGetStartedClick(user, password = state.password ?: "")) },
        user = registerObject
    )}

@Composable
fun CreateTextField(hint: String, onEvent: (String) -> Unit, stringValue: String) {

    TextField(
        value = stringValue,
        onValueChange = { onEvent(it) },
        placeholder = {
            Text(text = hint, color = Color.Gray)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = AbsoluteRoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Gray,
            disabledTextColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )
    )
}


@Preview
@Composable
fun RegisterScreenPreview() {
    val state = RegisterState(
        email = "scott@email.com",
        password = "password",
        firstName = "Scott",
        lastName = "Robinson",
        phoneNumber = "4706675061",
        userId = "userID234",
        companyAddress = "Company Address",
        companyName = "Company name",
        isEmailValid = true,
        isPasswordVisible = true,
        isRegistrationSuccessful = true,
        isLoginSuccessful = false,
        isLoading = false
    )
    RegisterScreen(state = state, onEvent = {})
}

@Composable
fun RegisterButton(
    onClick: (User) -> Unit,
    user: User
) {
    Button(
        onClick = {
            onClick(user)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .width(200.dp)
            .wrapContentHeight(),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(GradientPink.value),
                            Color(DbmPurple.value),
                            Color(GradientDarkPurple.value)
                        )
                    )
                )
                .width(200.dp)
                .wrapContentHeight()
                .padding(top = 10.dp, bottom = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = stringResource(R.string.register), color = Color.White)

        }
    }
}
