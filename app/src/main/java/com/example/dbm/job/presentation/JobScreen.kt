package com.example.dbm.job.presentation

import android.content.ContentValues
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.example.dbm.R
import com.example.dbm.job.constants.QuestionIds
import com.example.dbm.login.presentation.ActionButton
import com.example.dbm.login.presentation.CreateSpinner
import com.example.dbm.main.presentation.CreateTopBar
import com.example.dbm.main.presentation.DropdownMenuItemText
import com.example.dbm.presentation.theme.DbmPurple


@Composable
fun JobScreen (
    state: JobState,
    onEvents: (JobEvents) -> Unit,
    otherQuestionState: QuestionState,
    questionStateList: List<QuestionState>,
    scopeOfWorkCheckBoxState: List<CheckBoxState>,
    singleLineCheckBoxState: List<CheckBoxState>,
    siteInfoStateList: List<QuestionState>,
    sitePhotoStateList: List<PhotoState>
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
                if (state.shouldShowSaveDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            onEvents(JobEvents.ToggleShouldSaveMenu(false)) // Toggle to hide dialog
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                onEvents(JobEvents.OnSaveUnsubmittedJob) // Trigger save action
                                onEvents(JobEvents.ToggleShouldSaveMenu(false)) // Toggle to hide dialog after saving
                            }) {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                onEvents(JobEvents.ToggleShouldSaveMenu(false)) // Toggle to hide dialog
                            }) {
                                Text("No")
                            }
                        },
                        title = { Text("Save Job?") },
                        text = { Text("Would you like to save the job before leaving?") }
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
                            val subTextList = mutableListOf(
                                stringResource(
                                    R.string.licensed_stamps_and_letters
                                ),
                                stringResource(
                                    R.string.license_stamps
                                ),
                                stringResource(
                                    R.string.does_not_include_engineering_stamps
                                )
                            )
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
                            createLabel(label = stringResource(R.string.for_single_pages))
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
                            SitePictures(
                                stateList = sitePhotoStateList.toMutableList(),
                                event = { uri, questionId, questionTxt ->
                                    onEvents(JobEvents.OnPhotoAdded(
                                        uri = uri,
                                        questionId = questionId,
                                        questionTxt = questionTxt
                                    )
                                    )
                                },
                                removePhotoAction = {
                                    uri, questionId ->
                                    onEvents(JobEvents.RemovePhoto(uri, questionId))
                                }
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CreateSpinner(
                    isDisplay = state.showSpinner,
                    displayText = stringResource(R.string.saving_job)
                )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    ActionButton(
                        onClick = { onEvents(JobEvents.OnSaveJob) },
                        text = stringResource(R.string.submit_project),
                        width = 300.dp,
                    )
                }
            }
        }
    )
}

@Composable
fun CreateOtherSection(
    action: (String, QuestionIds, String) -> Unit,
    otherQuestionState: QuestionState
) {
    createLabel(label = stringResource(R.string.other))
    CreateTextFieldForJobScreen(
        hint = stringResource(R.string.please_provide_a_detailed_description_of_the_services_you_are_requesting),
        onEvent = { text -> action(text, otherQuestionState.questionId ?: QuestionIds.NULL_QUESTION_ID, text) },
        response = otherQuestionState.response ?: ""
    )
}

@Composable
fun SitePictures(
    stateList: MutableList<PhotoState>,
    event: (Uri, QuestionIds, String) -> Unit,
    removePhotoAction: (Uri, QuestionIds) -> Unit
) {
    createLabel(label = stringResource(R.string.site_photos))

    val context = LocalContext.current
    var selectedIndex by remember { mutableStateOf<Int?>(null) } // Declare a variable to hold the selected index
    var newPhotoUri by remember { mutableStateOf<Uri?>(null) } // Holds the URI for the new photo
    var showDialog by remember { mutableStateOf(false) } // To control the dialog visibility
    var selectedUri by remember { mutableStateOf<Uri?>(null) } // Track the currently selected image


    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedIndex?.let { index ->
                uri?.let {
                    event(
                        it,
                        stateList[index].questionId ?: QuestionIds.NULL_QUESTION_ID,
                        stateList[index].question ?: ""
                    )
                }
            }
        }
    )

    fun createImageUri(): Uri {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "photo_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, context.getString(R.string.image_jpeg))
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
        return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success && newPhotoUri != null) {
                selectedIndex?.let { index ->
                    event(
                        newPhotoUri!!,
                        stateList[index].questionId ?: QuestionIds.NULL_QUESTION_ID,
                        stateList[index].question ?: ""
                    )
                }
            }
        }
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.select_an_option)) },
            text = { Text(stringResource(R.string.choose_to_take_a_picture_or_select_from_gallery)) },
            confirmButton = {
                TextButton(onClick = {
                    selectedIndex?.let {
                        newPhotoUri = createImageUri() // Create a URI for the new image
                        cameraLauncher.launch(newPhotoUri) // Launch camera
                    }
                    showDialog = false
                }) {
                    Text(stringResource(R.string.camera))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    selectedIndex?.let {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                    showDialog = false
                }) {
                    Text(stringResource(R.string.gallery))
                }
            }
        )
    }

    Column {
        stateList.forEachIndexed { index, photoState ->
            Text(text = photoState.question ?: "")
            Spacer(modifier = Modifier.height(5.dp))
            Divider(thickness = 3.dp, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.add_icon),
                    contentDescription = stringResource(R.string.add_image),
                    modifier = Modifier.clickable {
                        selectedIndex = index
                        showDialog = true // Show the dialog when the icon is clicked
                    }
                )
                Spacer(modifier = Modifier.width(10.dp))
                photoState.imageUri?.forEach { uri ->
                    createImage(
                        uri,
                        shouldShowMenu = selectedUri == uri,
                        onPhotoClicked = { isMenuVisible ->
                            selectedUri = if (isMenuVisible) uri else null
                        },
                        removePhotoAction = removePhotoAction,
                        questionIds = photoState.questionId ?: QuestionIds.NULL_QUESTION_ID)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
@Composable
fun createLabel(label: String) {
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = label, fontWeight = Bold, fontSize = 15.sp)
    Spacer(modifier = Modifier.height(5.dp))
}

@Composable
fun createImage(
    uri: Uri,
    shouldShowMenu: Boolean,
    onPhotoClicked: (Boolean) -> Unit,
    removePhotoAction: (Uri, QuestionIds) -> Unit,
    questionIds: QuestionIds
) {
    var showDialog by remember { mutableStateOf(false) } // To control the dialog visibility

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(uri)
            .scale(Scale.FIT)
            .build(),
        contentDescription = null,
        modifier = Modifier
            .size(55.dp)
            .clickable {
                showDialog = true
                onPhotoClicked(!shouldShowMenu)
            },
        contentScale = ContentScale.Crop
    )
    Spacer(modifier = Modifier.width(5.dp))

    if (showDialog) {
        Surface {
            DropdownMenu(
                expanded = shouldShowMenu,
                onDismissRequest = {
                    onPhotoClicked(!shouldShowMenu)
                },
                modifier = Modifier.background(
                    color = DbmPurple,
                    shape = RoundedCornerShape(8.dp)
                )
            ) {
                Box {
                    DropdownMenuItem(
                        text = {
                            DropdownMenuItemText(itemTitle = stringResource(R.string.remove_photo))
                        },
                        onClick = {
                            onPhotoClicked(!shouldShowMenu)
                            removePhotoAction(uri, questionIds)
                        },
                        modifier = Modifier
                            .background(color = Color.LightGray)
                    )
                }
            }
        }
    }
}

@Composable
fun SiteInformation(
    onEvents: (String, QuestionIds, String) -> Unit,
    stateList: MutableList<QuestionState>
) {
    createLabel(label = stringResource(id = R.string.site_information))

    stateList.forEach { questionState ->
        CreateTextFieldForJobScreen(
            hint = questionState.questionTxt ?: "",
            onEvent = { text ->
                onEvents(text, questionState.questionId ?: QuestionIds.NULL_QUESTION_ID, questionState.questionTxt.toString())
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
   createLabel(label = stringResource(R.string.project_details))

    stateList.forEach { questionState ->
        CreateTextFieldForJobScreen(
            hint = questionState.questionTxt ?: "",
            onEvent = { text ->
                onEvents(text, questionState.questionId ?: QuestionIds.NULL_QUESTION_ID, questionState.questionTxt.toString())
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
                    Text(text = item.text ?: "")
                    if (subTextList != null && index < subTextList.size) {
                        Text(text = subTextList[index], fontSize = 10.sp)
                    }
                }
                Checkbox(
                    checked = item.isChecked ?: false,
                    onCheckedChange = { isChecked ->
                        buttonList[index] = item.copy(
                            isChecked = isChecked,
                            questionId = item.questionId
                        )
                      action(item.text ?: "", item.questionId ?: QuestionIds.NULL_QUESTION_ID, item.text ?: "", isChecked, index)
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
    val questionStates = mutableListOf(
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
        CheckBoxState(isChecked = false, text = "Structural Engineering", questionId = QuestionIds.STRUCTURAL_ENGINEERING),
        CheckBoxState(isChecked = false, text = "Electrical Engineering", questionId = QuestionIds.STRUCTURAL_ENGINEERING),
        CheckBoxState(isChecked = false, text = "Full Permitting Packet", questionId = QuestionIds.STRUCTURAL_ENGINEERING),
    )

    val singleLineCheckBoxList = mutableListOf(
        CheckBoxState(isChecked = false, text = "Single Line Electrical", questionId = QuestionIds.STRUCTURAL_ENGINEERING),
        CheckBoxState(isChecked = false, text = "Three Line Electrical", questionId = QuestionIds.STRUCTURAL_ENGINEERING),
        CheckBoxState(isChecked = false, text = "Site Plan/Layouts", questionId = QuestionIds.STRUCTURAL_ENGINEERING)
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

    val sitePhotoList = mutableListOf(
        PhotoState(questionId = QuestionIds.SERVICE_PANEL_LOCATION_PIC, question = "Location of Service Panel", imageUri = null),
        PhotoState(questionId = QuestionIds.METER_NUMBER_PIC, question = "Meter Number", imageUri = null),
        PhotoState(questionId = QuestionIds.MAIN_BREAKER_LOCATION_PIC, question = "Main Breaker Location", imageUri = null),
        PhotoState(questionId = QuestionIds.PANEL_DATA_PIC, question = "Panel Data Picture (Legible)", imageUri = null),
        PhotoState(questionId = QuestionIds.OPEN_PLATE_SERVICE_PANEL_PIC, question = "Open Plate Service Panel", imageUri = null),
        PhotoState(questionId = QuestionIds.SUPPORT_SPACING_PIC, question = "Support Spacing", imageUri = null),
        PhotoState(questionId = QuestionIds.SUPPORT_PHOTOGRAPH, question = "Support Photograph", imageUri = null),
        PhotoState(questionId = QuestionIds.OBSTACLES_PIC, question = "Obstacles", imageUri = null),
        PhotoState(questionId = QuestionIds.FULL_ROOF_PIC, question = "Full Roof View", imageUri = null)
    )

    JobScreen(state = state,
        onEvents = {},
        otherQuestionState = otherQuestionState,
        scopeOfWorkCheckBoxState = checkBoxStateList,
        singleLineCheckBoxState = singleLineCheckBoxList,
        siteInfoStateList = siteInfoList,
        questionStateList = questionStates,
        sitePhotoStateList = sitePhotoList
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