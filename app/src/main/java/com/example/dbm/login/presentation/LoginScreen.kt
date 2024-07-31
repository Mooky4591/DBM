package com.example.dbm.login.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dbm.R
import com.example.dbm.login.presentation.objects.Login
import com.example.dbm.presentation.theme.DbmPurple
import com.example.dbm.presentation.theme.GradientDarkPurple
import com.example.dbm.presentation.theme.GradientPink

@Composable
fun LoginScreen(
    state: LoginState,
    onEvent: (LoginEvents) -> Unit
) {
    Scaffold {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxHeight()
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
            Surface(
                modifier = Modifier
                    .fillMaxHeight(),
                shape = AbsoluteRoundedCornerShape(60.dp, 60.dp, 900.dp, 0.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxHeight(.75f)
                        .padding(30.dp)
                ) {
                    CreateEmailField(
                        isEmailValid = state.isEmailValid,
                        onValueChange = { email -> onEvent(LoginEvents.OnEmailChanged(email)) },
                        email = state.email
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    CreatePasswordField(
                        password = state.password,
                        onValueChange = { password -> onEvent(LoginEvents.OnPasswordChanged(password)) },
                        isPasswordVisible = state.isPasswordVisible,
                        onClick = { isPasswordVisible ->
                            onEvent(
                                LoginEvents.OnTogglePasswordVisibility(
                                    isPasswordVisible
                                )
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ActionButton(
                                onClick = { onEvent(LoginEvents.OnLoginClick) },
                                text = stringResource(
                                    id = R.string.login
                                ),
                                width = 200.dp
                            )
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CreateSignUpLink(onEvent = { onEvent(LoginEvents.OnRegisterLinkClick) })
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        CreateSpinner(isDisplay = state.isLoggingIn)
                    }
                }
            }
        }
    }
}

@Composable
fun TopLogo() {
    Image(painter = painterResource(id = R.drawable.dbmlogo),
        contentDescription = "logo",
        alignment = Alignment.TopCenter,
        modifier = Modifier.padding(top = 25.dp, bottom = 45.dp)
    )
}

@Composable
fun CreateEmailField(isEmailValid: Boolean, onValueChange: (String) -> Unit, email: String) {
    TextField(
        value = email,
        onValueChange = {
            onValueChange(it)
        },
        placeholder = {
            Text(text = stringResource(R.string.email_address), color = Color.Gray)
        },
        trailingIcon = {
            val image = R.drawable.check_mark
            val description = if (isEmailValid) "Email is valid checkmark" else "email not valid"
            if (isEmailValid) {
                Icon(painter = painterResource(id = image), contentDescription = description)
            }
        },
        singleLine = true,
        modifier = if (isEmailValid || email.isEmpty())
            Modifier
                .fillMaxWidth()
        else Modifier
            .fillMaxWidth()
            .border(
                width = 1.5.dp,
                color = Color.Red,
                shape = AbsoluteRoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp)
            ),
        shape = AbsoluteRoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp)
    )
}

@Composable
fun CreatePasswordField(password: String, onValueChange: (String) -> Unit, isPasswordVisible: Boolean, onClick: (Boolean) -> Unit) {

    TextField (
        value = password,
        onValueChange = {
            onValueChange(it)
        },
        placeholder = {
            Text(text = stringResource(R.string.password), color = Color.Gray)
        },
        shape = AbsoluteRoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp),
        visualTransformation = showOrHidePassword(isPasswordVisible = isPasswordVisible),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            CreateHidePasswordToggle(isPasswordVisible = isPasswordVisible, onClick = {isPasswordVisible -> onClick(isPasswordVisible)})
        }
    )
}
@Composable
fun showOrHidePassword(isPasswordVisible: Boolean): VisualTransformation {
    return if (!isPasswordVisible) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }
}


@Composable
fun CreateHidePasswordToggle(isPasswordVisible: Boolean, onClick: (Boolean) -> Unit) {
    val image = if (isPasswordVisible)
        R.drawable.show_password
    else R.drawable.hide_password

    // Localized description for accessibility services
    val description = if (isPasswordVisible) stringResource(R.string.hide_password) else stringResource(
        R.string.show_password
    )

    // Toggle button to hide or display password
    IconButton(onClick = {
        onClick(!isPasswordVisible)
    })
    {
        Icon(
            painter = painterResource(id = image),
            contentDescription = description)
    }
}

@Composable
fun ActionButton(
    onClick: () -> Unit,
    text: String,
    width: Dp
) {
    Button(
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .width(width)
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
                .width(width)
                .wrapContentHeight()
                .padding(top = 10.dp, bottom = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, color = Color.White)

        }
    }
}

@Composable
fun CreateSignUpLink(onEvent: (LoginEvents) -> Unit) {
    return Text(
        fontSize = 12.sp,
        text = "SIGN UP",
        modifier = Modifier.clickable {
            onEvent(LoginEvents.OnRegisterLinkClick)
        },
        color = DbmPurple,
        fontStyle = FontStyle.Italic
    )
}

@Composable
fun CreateSpinner(
    isDisplay: Boolean
) {
    val focusManager = LocalFocusManager.current
    if (isDisplay) {
        CircularProgressIndicator(
            color = DbmPurple,
            modifier = Modifier
                .height(75.dp)
                .width(75.dp),
            strokeWidth = 10.dp,
            trackColor = Color.Transparent,
            strokeCap = StrokeCap.Round
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Loading...",
            fontSize = 15.sp
        )
        focusManager.clearFocus()
    }
}