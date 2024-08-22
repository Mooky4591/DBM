package com.example.dbm.presentation.edit_text.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dbm.login.presentation.CreateSpinner
import com.example.dbm.main.presentation.CreateTopBar
import com.example.dbm.register.presentation.CreateTextField

@Composable
fun EditTextScreen(
    state: EditTextState,
    onEvent: (EditTextEvent) -> Unit
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
                     backPressed = { onEvent(EditTextEvent.OnBackPress) },
                     name = state.text ?: "",
                     isUserSettingsDropDownExpanded = null,
                     userSettingsPressed = {},
                     shouldShowSettingsButton = false,
                     shouldShowSaveButton = true,
                     save = { onEvent(EditTextEvent.OnSavePressed) }
                 )
             }
             Surface(
                 modifier = Modifier
                     .fillMaxSize(),
                 shape = AbsoluteRoundedCornerShape(60.dp, 60.dp, 900.dp, 0.dp)
             ) {
                 Column(
                     horizontalAlignment = Alignment.CenterHorizontally,
                     modifier = Modifier.padding(start = 30.dp, end = 30.dp)
                 ) {
                     UnderlinedTitleText(
                         text = state.title ?: "",
                         textAlignment = TextAlign.Center,
                         fontSize = 20.sp,
                         topPadding = 10,
                         bottomPadding = 10
                     )
                     CreateTextField(
                         hint = "change text",
                         onEvent = { text ->
                             onEvent(EditTextEvent.OnTextChanged(text))},
                         stringValue = state.text ?: ""
                     )
                     Spacer(modifier = Modifier.height(10.dp))
                     CreateSpinner(isDisplay = state.isLoggingIn)
                 }
             }
         }
     }
    )
}

@Composable
fun UnderlinedTitleText(text: String, textAlignment: TextAlign, fontSize: TextUnit, topPadding: Int, bottomPadding: Int) {
    Text(
        text = text,
        textAlign = textAlignment,
        textDecoration = TextDecoration.Underline,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = topPadding.dp, bottom = bottomPadding.dp)
    )
}

@Preview
@Composable
fun EditTextScreenPreview(){
    val state = EditTextState(
        title = "Change Company Name",
        text = "DBMSolar",
        name = "Chase Daily"
    )
    EditTextScreen(state = state, onEvent = {})
}
