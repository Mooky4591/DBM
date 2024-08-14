package com.example.dbm.job.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dbm.R
import com.example.dbm.login.presentation.ActionButton
import com.example.dbm.main.presentation.CreateTopBar


@Composable
fun JobScreen (
    state: JobState,
    onEvents: (JobEvents) -> Unit
) {
    Scaffold (
        content = {
            Column (
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
                            backPressed = { onEvents(JobEvents.OnBackPress) },
                            name = state.name ?: "",
                            isUserSettingsDropDownExpanded = state.isUserSettingsSelected,
                            userSettingsPressed = { isUserDropDownExpanded ->
                                onEvents(JobEvents.OnUserSettingsSelected(isUserDropDownExpanded))
                            },
                            shouldShowSettingsButton = false,
                            shouldShowSaveButton = true,
                        )
                    }
                Box (modifier = Modifier.fillMaxSize()) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
                        shape = AbsoluteRoundedCornerShape(60.dp, 60.dp, 900.dp, 0.dp)
                    ) {
                        val scrollState = rememberScrollState()
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top,
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(10.dp)
                                .verticalScroll(scrollState)
                        ) {
                            JobFormTitle()
                            /*
                            ScopeOfWorkQuestions()
                            SinglePagesQuestions()
                            ProjectDetails()
                            SiteInformation()
                            ElectricalBoxMainServicePanelPictures()
                            StructuralDetailAndMeasurementPictures()
                            RoofDetailAndObstaclesPictures()
                             */
                        }
                    }
                }
            }
        },
        bottomBar = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                ActionButton(
                    onClick = { /*TODO*/ },
                    text = stringResource(R.string.submit_project),
                    width = 300.dp,
                )
            }
        }
    )
}

@Composable
fun RoofDetailAndObstaclesPictures() {
    TODO("Not yet implemented")
}

@Composable
fun StructuralDetailAndMeasurementPictures() {
    TODO("Not yet implemented")
}

@Composable
fun ElectricalBoxMainServicePanelPictures() {
    TODO("Not yet implemented")
}

@Composable
fun SiteInformation() {
    TODO("Not yet implemented")
}

@Composable
fun ProjectDetails() {
    TODO("Not yet implemented")
}

@Composable
fun SinglePagesQuestions() {
    TODO("Not yet implemented")
}

@Composable
fun ScopeOfWorkQuestions() {
    TODO("Not yet implemented")
}

@Composable
fun JobFormTitle() {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = stringResource(R.string.scope_of_work), fontWeight = Bold)
        Spacer(modifier = Modifier.height(3.dp))
        Text(text = stringResource(R.string.check_all_that_apply), fontWeight = Bold)
        Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = stringResource(R.string.required_info_text),
                fontSize = 8.sp,
                color = Color.Red,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center)
    }
}

@Preview
@Composable
fun JobScreenPreview(){
    val state = JobState(
        name = "Scott Robinson",
        email = "scottrobinson4591@gmail.com",
        isUserSettingsSelected = false,
    )
    JobScreen(state = state, onEvents = {})
}