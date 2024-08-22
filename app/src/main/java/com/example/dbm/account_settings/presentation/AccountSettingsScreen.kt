package com.example.dbm.account_settings.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dbm.main.presentation.CreateTopBar
import com.example.dbm.register.presentation.CreateTextField
import com.example.dbm.R

@Composable
fun AccountSettingsScreen(
    state: SettingState,
    onEvent: (AccountSettingEvent) -> Unit
) {
    Scaffold(
        content = {
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
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, bottom = 5.dp)
                ) {
                    CreateTopBar(
                        backPressed = { onEvent(AccountSettingEvent.OnBackPress) },
                        name = state.name ?: "",
                        isUserSettingsDropDownExpanded = null,
                        userSettingsPressed = {},
                        shouldShowSettingsButton = false,
                        shouldShowSaveButton = true,
                    )
                }
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    shape = AbsoluteRoundedCornerShape(60.dp, 60.dp, 900.dp, 0.dp)
                ) {
                        val scrollState = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .verticalScroll(
                                    state = scrollState
                                )
                                .fillMaxSize()
                                .padding(start = 30.dp, end = 30.dp)
                        ) {
                            CreateSettingsLabel()
                            //change email field
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(text = stringResource(id = R.string.change_email), fontSize = 10.sp, color = Color.Gray)
                            TextFieldWithNavigation(
                                hint = state.email ?: "",
                                onClick = { email ->
                                    onEvent(AccountSettingEvent.OnEmailChangeClicked(email.toString()))
                                },
                                stringValue = state.email ?: ""
                            )

                            //change phone number field
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = stringResource(id = R.string.change_phone_number), fontSize = 10.sp, color = Color.Gray)
                            TextFieldWithNavigation(
                                hint = stringResource(
                                    id = R.string.change_phone_number
                                ), onClick = { number ->
                                    onEvent(AccountSettingEvent.OnPhoneNumberChangeClicked(number.toString()))
                                },
                                stringValue = state.phoneNumber ?: "")

                            //change name field
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = stringResource(id = R.string.change_name), fontSize = 10.sp, color = Color.Gray)
                            TextFieldWithNavigation(
                                hint = stringResource(id = R.string.change_name),
                                onClick = { name ->
                                    onEvent(AccountSettingEvent.OnNameChangeClicked(name.toString()))
                                },
                                stringValue = state.name ?: ""
                            )

                            //change password field
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = stringResource(id = R.string.change_password), fontSize = 10.sp, color = Color.Gray)
                            TextFieldWithNavigation(
                                hint = stringResource(
                                    id = R.string.change_password
                                ), onClick = {
                                    onEvent(AccountSettingEvent.OnChangePasswordClicked)
                                },
                                stringValue = "")

                            //change company name field
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = stringResource(id = R.string.change_company_name), fontSize = 10.sp, color = Color.Gray)
                            TextFieldWithNavigation(
                                hint = stringResource(
                                    id = R.string.company_name
                                ), onClick = { name ->
                                    onEvent(AccountSettingEvent.OnCompanyNameChangeClicked(name.toString()))
                                },
                                stringValue = state.companyName ?: "")

                            //change company address field
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = stringResource(id = R.string.change_company_address), fontSize = 10.sp, color = Color.Gray)
                            TextFieldWithNavigation(
                                hint = stringResource(
                                    id = R.string.change_company_address
                                ), onClick = { address ->
                                    onEvent(AccountSettingEvent.OnCompanyAddressChangeClicked(address.toString()))
                                },
                                stringValue = state.companyAddress ?: "")
                        }
                    }
                }

        }
    )
}

@Preview
@Composable
fun AccountSettingsScreenPreview() {
    val state = SettingState(
        userId = "userId123",
        name = "Scott Robinson",
        email = "Scott@email.com",
        phoneNumber = "4706675061",
        companyName = "Company Name",
        companyAddress = "Company Address"
    )
    AccountSettingsScreen(state = state, onEvent = {})
}

@Composable
fun TextFieldWithNavigation(onClick: (Any?) -> Unit, hint: String, stringValue: String){
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // The TextField displays the text
        CreateTextField(
            hint = hint, onEvent = {},
            stringValue = stringValue
        )
        // A transparent overlay that captures clicks
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable {
                    onClick(stringValue)
                }
        )
    }
}

@Composable
fun CreateSettingsLabel() {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 50.dp, top = 40.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )
        {
            drawLine(
                start = Offset(0f, size.height / 2),
                end = Offset(size.width, size.height / 2),
                color = Color.Black,
                strokeWidth = 1.dp.toPx()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))  // Add space between the line and the text

        Text(
            text = "Account Settings",
            modifier = Modifier.padding(
                end = 50.dp
            ),
            fontWeight = FontWeight.Bold
        )
    }
}
