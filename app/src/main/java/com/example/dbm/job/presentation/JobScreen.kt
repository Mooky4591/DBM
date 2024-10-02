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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Checkbox
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
import com.example.dbm.job.constants.QuestionIds
import com.example.dbm.login.presentation.ActionButton
import com.example.dbm.main.presentation.CreateTopBar


@Composable
fun JobScreen (
    state: JobState,
    onEvents: (JobEvents) -> Unit,
    otherQuestionState: QuestionState,
    questionStateList: List<QuestionState>,
    scopeOfWorkCheckBoxState: List<CheckBoxState>,
    singleLineCheckBoxState: List<CheckBoxState>,
    siteInfoStateList: List<QuestionState>
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
                            userSettingsPressed = {},
                            shouldShowSettingsButton = false,
                            shouldShowSaveButton = true,
                        )
                    }
                Box (modifier = Modifier.fillMaxSize()) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
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
                            val subTextList = mutableListOf("Licensed Stamps and Letters", "License Stamps", "Does Not Include Engineering Stamps")
                            CreateCheckListGroup(
                                action = {
                                         scopeOfWorkString, questionIds, questionTxt, isChecked, index ->
                                            onEvents(
                                                JobEvents.OnCheckMarkSelected(
                                                    response = scopeOfWorkString,
                                                    questionIds = questionIds,
                                                    questionTxt = questionTxt,
                                                    isChecked = isChecked,
                                                    index = index)
                                            )
                                },
                                buttonList = scopeOfWorkCheckBoxState.toMutableList(),
                                subTextList = subTextList,
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "For single pages:", fontWeight = Bold)
                            CreateCheckListGroup(
                                action = {
                                        scopeOfWorkString, questionIds, questionTxt, isChecked, index ->
                                    onEvents(
                                        JobEvents.OnCheckMarkSelected(
                                            response = scopeOfWorkString,
                                            questionIds = questionIds,
                                            questionTxt = questionTxt,
                                            isChecked = isChecked,
                                            index = index
                                        )
                                    )
                                },
                                buttonList = singleLineCheckBoxState.toMutableList(),
                                subTextList = null,
                            )
                            CreateOtherSection(
                                action = {
                                        description, questionId, questionTxt ->
                                    onEvents(
                                        JobEvents.OnQuestionAnswered(
                                            response = description,
                                            questionId = questionId,
                                            questionTxt = questionTxt
                                        )
                                    )
                                },
                                otherQuestionState = otherQuestionState
                            )
                            ProjectDetails(
                                onEvents = {
                                        response, questionId, questionTxt ->
                                    onEvents(
                                        JobEvents.OnQuestionAnswered(
                                            response = response,
                                            questionId = questionId,
                                            questionTxt = questionTxt
                                        )
                                    )
                                },
                                stateList = questionStateList.toMutableList()
                            )
                            SiteInformation(
                                onEvents = {
                                    response, questionId, questionTxt ->
                                onEvents(
                                    JobEvents.OnQuestionAnswered(
                                        response = response,
                                        questionId = questionId,
                                        questionTxt = questionTxt
                                    )
                                )
                            },
                                stateList = siteInfoStateList.toMutableList()
                            )
                            /*
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
fun CreateOtherSection(
    action: (String, QuestionIds, String) -> Unit,
    otherQuestionState: QuestionState
) {

    Spacer(modifier = Modifier.height(8.dp))
    Text(text = "Other: ", fontWeight = Bold, fontSize = 15.sp)
    Spacer(modifier = Modifier.height(5.dp))
    CreateTextFieldForJobScreen(
        hint = "Please provide a detailed description of the services you are requesting",
        onEvent = { text -> action(text, otherQuestionState.questionId!!, text) },
        response = otherQuestionState.response ?: ""
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
fun SiteInformation(
    onEvents: (String, QuestionIds, String) -> Unit,
    stateList: MutableList<QuestionState>
) {
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = "Site Information: ", fontWeight = Bold, fontSize = 15.sp)
    Spacer(modifier = Modifier.height(5.dp))

    stateList.forEach { questionState ->
        CreateTextFieldForJobScreen(
            hint = questionState.questionTxt ?: "",
            onEvent = { text ->
                onEvents(text, questionState.questionId!!, questionState.questionTxt.toString())
                      },
            response = questionState.response ?: ""
        )
        Spacer(modifier = Modifier.height(5.dp))
    }
}

@Composable
fun ProjectDetails(
    onEvents: (String, QuestionIds, String) -> Unit,
    stateList: MutableList<QuestionState>
) {

    Spacer(modifier = Modifier.height(8.dp))
    Text(text = "Project Details:", fontWeight = Bold)
    Spacer(modifier = Modifier.height(5.dp))
    stateList.forEach { questionState ->
        CreateTextFieldForJobScreen(
            hint = questionState.questionTxt ?: "",
            onEvent = { text ->
                onEvents(text, questionState.questionId!!, questionState.questionTxt.toString())
                      },
            response = questionState.response ?: "",
        )
        Spacer(modifier = Modifier.height(5.dp))
    }
}

@Composable
fun CreateCheckListGroup(
    action: (String, QuestionIds, String, Boolean, Int) -> Unit,
    buttonList: MutableList<CheckBoxState>,
    subTextList: List<String>?,
) {

    Spacer(modifier = Modifier.height(5.dp))
    buttonList.forEachIndexed { index, item ->
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .padding(start = 20.dp, end = 20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                modifier = Modifier
                    .background(Color.White)
                    .padding(start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
            ) {
                Column {
                    Text(text = item.text)
                    if (subTextList?.get(index) != null) {
                        Text(text = subTextList[index], fontSize = 10.sp)
                    }
                }
                Checkbox(
                    checked = item.isChecked,
                    onCheckedChange = { isChecked ->
                        buttonList[index] = item.copy(
                            isChecked = isChecked,
                            questionid = item.questionid
                        )
                      action(item.text, item.questionid, item.text, isChecked, index)
                    }
                )
            }
            Divider()
        }
        Spacer(modifier = Modifier.height(2.dp))
    }
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
    val _questionStates = mutableListOf(
        QuestionState(questionId = QuestionIds.PROJECT_NAME, questionTxt = "Project/Customer Name", response = null),
        QuestionState(questionId = QuestionIds.PROJECT_ADDRESS, questionTxt = "Project Address", response = null),
        QuestionState(questionId = QuestionIds.SYSTEM_SIZE, questionTxt = "System Size", response = null),
        QuestionState(questionId = QuestionIds.MAKE_MODEL_PANEL, questionTxt = "Make and Model of Panel", response = null),
        QuestionState(questionId = QuestionIds.RACKING_SYSTEM, questionTxt = "Racking System", response = null),
        QuestionState(questionId = QuestionIds.SUBMISSION_DATE, questionTxt = "Submission Date", response = null),
    )

    val otherQuestionState = QuestionState(
        questionId = QuestionIds.OTHER,
        questionTxt = stringResource(id = R.string.required_info_text),
        response = null
    )

    val checkBoxStateList = mutableListOf(
        CheckBoxState(isChecked = false, text = "Structural Engineering", questionid = QuestionIds.STRUCTURAL_ENGINEERING),
        CheckBoxState(isChecked = false, text = "Electrical Engineering", questionid = QuestionIds.STRUCTURAL_ENGINEERING),
        CheckBoxState(isChecked = false, text = "Full Permitting Packet", questionid = QuestionIds.STRUCTURAL_ENGINEERING),
    )

    val singleLineCheckBoxList = mutableListOf(
        CheckBoxState(isChecked = false, text = "Single Line Electrical", questionid = QuestionIds.STRUCTURAL_ENGINEERING),
        CheckBoxState(isChecked = false, text = "Three Line Electrical", questionid = QuestionIds.STRUCTURAL_ENGINEERING),
        CheckBoxState(isChecked = false, text = "Site Plan/Layouts", questionid = QuestionIds.STRUCTURAL_ENGINEERING)
    )

    val siteInfoList = mutableListOf(
        QuestionState(questionId = QuestionIds.ROOF_PITCH, questionTxt = "Roof Pitch", response = null),
        QuestionState(questionId = QuestionIds.ROOFING_MATERIAL, questionTxt = "Roofing Material", response = null),
        QuestionState(questionId = QuestionIds.RAFTER_SIZE_SPACING, questionTxt = "Rafter/Truss Size and Spacing", response = null),
        QuestionState(questionId = QuestionIds.MAIN_SERVICE_PANEL_LOCATION, questionTxt = "Location of the Main Service Panel", response = null),
        QuestionState(questionId = QuestionIds.MAIN_BREAKER_SIZE, questionTxt = "Size of the Main Breaker:", response = null),
        QuestionState(questionId = QuestionIds.MAIN_BUS_RATING, questionTxt = "Rating of the Main Bus", response = null),
        QuestionState(questionId = QuestionIds.PROPOSED_INVERTER_LOCATION, questionTxt = "Proposed Location of the Inverter", response = null),
        QuestionState(questionId = QuestionIds.PROPOSED_INTERCONNECTION_METHOD, questionTxt = "Proposed Interconnection Method", response = null),
        QuestionState(questionId = QuestionIds.UTILITY_METER_LOCATION, questionTxt = "Location of Utility Meter", response = null),
        QuestionState(questionId = QuestionIds.LAYOUT_INFORMATION, questionTxt = "Layout Information", response = null),
        QuestionState(questionId = QuestionIds.ADDITIONAL_DETAILS, questionTxt = "Additional Details", response = null)
    )

    JobScreen(state = state,
        onEvents = {},
        otherQuestionState = otherQuestionState,
        scopeOfWorkCheckBoxState = checkBoxStateList,
        singleLineCheckBoxState = singleLineCheckBoxList,
        siteInfoStateList = siteInfoList,
        questionStateList = _questionStates
    )
}

@Composable
fun CreateTextFieldForJobScreen(
    hint: String,
    onEvent: (String) -> Unit,
    response: String,
) {

    TextField(
        value = response,
        onValueChange = {
            onEvent(it)
                        },
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