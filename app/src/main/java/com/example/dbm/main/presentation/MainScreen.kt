package com.example.dbm.main.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dbm.R
import com.example.dbm.job.constants.QuestionIds
import com.example.dbm.job.presentation.objects.Job
import com.example.dbm.presentation.theme.DbmPurple
import com.example.dbm.presentation.theme.GradientDarkPurple
import com.example.dbm.presentation.theme.GradientPink
import com.example.dbm.presentation.theme.MellowYellow
import java.time.LocalDate

@Composable
fun MainScreen (
    state: MainState,
    onEvent: (MainEvents) -> Unit,
    jobs: List<Job> = emptyList()
) {
    Scaffold (
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
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, bottom = 5.dp)
                ) {
                    CreateTopBar(
                        backPressed = { onEvent(MainEvents.OnBackPress) },
                        name = state.name ?: "",
                        isUserSettingsDropDownExpanded = state.isUserSettingsSelected,
                        userSettingsPressed = { isUserDropDownExpanded ->
                            onEvent(MainEvents.OnUserSettingsSelected(isUserDropDownExpanded))
                        },
                        shouldShowSettingsButton = true,
                        shouldShowSaveButton = false,
                        isSaveEnabled = false,
                        action = { event ->
                            when (event) {
                                MainEvents.OnAccountSettingsPressed -> {
                                    onEvent(MainEvents.OnAccountSettingsPressed)
                                }
                                MainEvents.OnLogoutPressed -> {
                                    onEvent(MainEvents.OnLogoutPressed)
                                }
                                MainEvents.OnContactUsPressed -> {
                                    onEvent(MainEvents.OnContactUsPressed)
                                }
                                else -> {}
                            }
                        },
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ){
                    CreateSquareIconButton(
                        icon = ImageVector.vectorResource(id = R.drawable.search),
                        contentDescription = "search",
                        onClick = { onEvent(MainEvents.OnSearchSelected) }
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    CreateSquareIconButton(
                        icon = ImageVector.vectorResource(id = R.drawable.history), 
                        contentDescription = "history",
                        onClick = { onEvent(MainEvents.StartNewProject) }
                        )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                shape = AbsoluteRoundedCornerShape(60.dp, 60.dp, 900.dp, 0.dp)
            ) {
                if (jobs.isNotEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            stringResource(R.string.unsubmitted_jobs),
                            fontSize = 18.sp,
                            color = Color.Black,
                            fontFamily = FontFamily.SansSerif,
                            style = TextStyle(
                                textDecoration = TextDecoration.Underline
                            )
                        )
                    }
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier
                            .fillMaxHeight(.75f)
                            .padding(30.dp)
                    ) {
                        items(jobs) { job ->
                            DisplayProject(
                                job = job,
                                selectedJobId = state.selectedJobId ?: "",
                                onClick = { onEvent(MainEvents.OnUnfinishedJobSelected(job.formId ?: "")) },
                                onElipsisSelected = { selectedJobId ->
                                    onEvent(MainEvents.ElipsisSelected(selectedJobId))
                                },
                                action = {onEvent(MainEvents.DeleteUnfinishedJob(job.formId!!))},
                                shouldShowHighPriorityIcon = true
                            )
                        }

                    }
                } else {
                    Row (
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize()
                    ) {
                            NoUnfinishedJobsText()
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
                IconActionButton(
                    onClick = {
                        onEvent(MainEvents.StartNewProject)
                    },
                    text = stringResource(R.string.start_new_job),
                    width = 300.dp,
                    image = R.drawable.plus
                )
            }
        }
    )
}

@Composable
fun DisplayProject(
    job: Job,
    onClick: () -> Unit,
    selectedJobId: String,
    onElipsisSelected: ((String?) -> Unit)?,
    action: (() -> Unit)?,
    shouldShowHighPriorityIcon: Boolean
) {
        var address: String? = null
        if (job.questionsAndAnswers?.isNotEmpty() == true) {
            for (question in job.questionsAndAnswers!!) {
                if (question.questionId == QuestionIds.PROJECT_ADDRESS) {
                    address = if(!question.answer.isNullOrEmpty()) {
                        question.answer
                    } else {
                        stringResource(R.string.no_address_entered)
                    }
                    break
                } else {
                    address = stringResource(R.string.no_address_entered)
                }
            }
        } else {
            address = stringResource(R.string.no_address_entered)
        }
        Card(
            colors = CardDefaults.cardColors(MellowYellow),
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .wrapContentSize()
                .padding(5.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(8.dp),
                    clip = false
                )
                .clickable {
                    onClick()
                }
        ) {
            Row(
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp)
            ) {
                if (shouldShowHighPriorityIcon) {
                    Image(
                        painter = painterResource(id = R.drawable.alert),
                        contentDescription = stringResource(
                            R.string.high_priority
                        )
                    )
                }
                Column {
                    Text(text = address ?: "")
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "Date Created: ${job.dateCreated}")
                }
                Surface (
                    color = Color.Transparent
                ) {
                    if (onElipsisSelected != null) {
                        Image(painter = painterResource(id = R.drawable.elipsis),
                            contentDescription = stringResource(
                                R.string.elipsis
                            ),
                            modifier = Modifier.clickable {
                                val newSelectedId =
                                    if (selectedJobId == job.formId) null else job.formId
                                if (onElipsisSelected != null) {
                                    onElipsisSelected(newSelectedId)
                                }
                            }
                        )
                    }
                    if (action != null && onElipsisSelected != null) {
                        CreateContextMenu(
                            expanded = selectedJobId == job.formId,
                            action = {
                                action()
                            },
                            dismiss = {
                                onElipsisSelected(null)
                            },
                            menuColor = DbmPurple,
                            menuItemColor = Color.LightGray,
                            jobId = job.formId!!
                        )
                    }
                }
            }

        }
    }

@Composable
fun NoUnfinishedJobsText() {
    Text(
        text = stringResource(R.string.no_unfinished_jobs),
        color = Color.Black,
        fontSize = 15.sp,
        fontFamily = FontFamily.SansSerif,
        style = TextStyle(
            textDecoration = TextDecoration.Underline
        )
    )
}

@Composable
fun CreateSquareIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier.size(100.dp), // Adjust size as needed
        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray) // Customize the button color if needed
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = DbmPurple
        )
    }
}

@Composable
fun IconActionButton(
    onClick: () -> Unit,
    text: String,
    width: Dp,
    image: Int
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
                Row(
                    horizontalArrangement = Arrangement.Center ,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter =
                        painterResource(
                            id = image
                        ),
                        contentDescription = "New Job",
                        modifier = Modifier
                            .height(25.dp)
                            .width(25.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = text, color = Color.White)
                }
            }
    }
}

    @Composable
    fun CreateTopBar(
        backPressed: () -> Unit,
        action: (Any) -> Unit = {},
        save: () -> Unit = {},
        userSettingsPressed: (Boolean) -> Unit = {},
        isUserSettingsDropDownExpanded: Boolean?,
        name: String,
        shouldShowSettingsButton: Boolean,
        shouldShowSaveButton: Boolean,
        isSaveEnabled: Boolean
    ) {
        CreateBackButton(backPressed)
        Text(
            text = name,
            fontSize = 30.sp,
            color = Color.White
        )
        if (shouldShowSettingsButton) {
            Surface(
                color = Color.Transparent
            ) {
                IconButton(
                    onClick = {
                        userSettingsPressed(!isUserSettingsDropDownExpanded!!)
                    }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.account_icon),
                        contentDescription = "account",
                    )
                }
                CreateDropDownMenu(
                    expanded = isUserSettingsDropDownExpanded ?: false,
                    dismiss = {
                        userSettingsPressed(!isUserSettingsDropDownExpanded!!)
                    },
                    menuColor = DbmPurple,
                    menuItemColor = Color.LightGray,
                    action = { event ->
                        when (event) {
                            MainEvents.OnAccountSettingsPressed -> {
                                action(MainEvents.OnAccountSettingsPressed)
                            }
                            MainEvents.OnLogoutPressed -> {
                                action(MainEvents.OnLogoutPressed)
                            }
                            MainEvents.OnContactUsPressed -> {
                                action(MainEvents.OnContactUsPressed)
                            }
                            else -> {}
                        }
                    }
                )
            }
        } else if (shouldShowSaveButton){
            CreateSaveButton(onEvent = save, isEnabled = isSaveEnabled)
        }
    }

@Composable
fun CreateBackButton(
    backPressed: () -> Unit,
    ) {
    IconButton(
        onClick = {
            backPressed()
        }
    ) {
        Image(
            painter = painterResource(id = R.drawable.arrow_back),
            contentDescription = "navigate back"
        )
    }
}

@Composable
fun CreateSaveButton(
    onEvent: () -> Unit,
    isEnabled: Boolean
) {
    Surface (
        color = Color.Transparent,
        modifier = Modifier.wrapContentSize()
    ) {
        TextButton(
            enabled = isEnabled,
            onClick = { onEvent() }
        ) {
            Text(
                text = stringResource(id = R.string.save),
                color = if (isEnabled) {
                    Color.White
                } else {
                    Color.Gray
                }
            )
        }
    }
}

@Composable
fun CreateUserSettingsIcon(
    userSettingsPressed: (Boolean) -> Unit,
    isUserSettingsDropDownExpanded: Boolean,
) {
    Surface (
        color = Color.Transparent,
        modifier = Modifier.wrapContentSize()
    ) {
        IconButton(
            onClick = {
                userSettingsPressed(!isUserSettingsDropDownExpanded)
            }
        ) {
            Image(
                painter = painterResource(id = R.drawable.account_icon),
                contentDescription = "account",
                colorFilter = ColorFilter.tint(Color.Transparent)
            )
        }
    }
}

@Composable
fun CreateDropDownMenu(
    expanded: Boolean,
    dismiss: (Boolean) -> Unit,
    action: (MainEvents) -> Unit,
    menuColor: Color,
    menuItemColor: Color
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { dismiss(!expanded) },
        modifier = Modifier.background(menuColor)
    ) {
        Divider(thickness = .5.dp, color = Color.Black)
        Box {
            DropdownMenuItem(
                text = {
                    DropdownMenuItemText(itemTitle = stringResource(R.string.account_settings))
                },
                onClick = {
                    action(MainEvents.OnAccountSettingsPressed)
                },
                modifier = Modifier
                    .background(
                        color = menuItemColor,
                        shape = RoundedCornerShape(8.dp)
                    )
            )
        }
        Divider(thickness = .5.dp, color = Color.Black)
        Box {
            DropdownMenuItem(
                text = {
                    DropdownMenuItemText(itemTitle = stringResource(R.string.contact_us))
                },
                onClick = {
                    action(MainEvents.OnContactUsPressed)
                },
                modifier = Modifier.background(color = menuItemColor)
            )
        }
        Divider(thickness = .5.dp, color = Color.Black)
        Box {
            DropdownMenuItem(
                text = {
                    DropdownMenuItemText(itemTitle = stringResource(R.string.logout))
                },
                onClick = {
                    action(MainEvents.OnLogoutPressed)
                },
                modifier = Modifier.background(color = menuItemColor)
            )
        }
        Divider(thickness = .5.dp, color = Color.Black)
    }
}

@Composable
fun CreateContextMenu(
    expanded: Boolean,
    dismiss: (Any?) -> Unit,
    action: (MainEvents) -> Unit,
    menuColor: Color,
    menuItemColor: Color,
    jobId: String
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { dismiss(null) },
        modifier = Modifier.background(menuColor)
    ) {
        Divider(thickness = .5.dp, color = Color.Black)
        Box {
            DropdownMenuItem(
                text = {
                    DropdownMenuItemText(itemTitle = stringResource(R.string.delete))
                },
                onClick = {
                    action(MainEvents.DeleteUnfinishedJob(jobId))
                },
                modifier = Modifier
                    .background(
                        color = menuItemColor,
                        shape = RoundedCornerShape(8.dp)
                    )
            )
        }
        Divider(thickness = .5.dp, color = Color.Black)
    }
}

@Composable
fun DropdownMenuItemText(itemTitle: String) {
    Text(text = itemTitle, color = Color.Black)
}


@Preview
@Composable
fun MainScreenPreview(){
    val state = MainState(
        name = "Scott Robinson",
        email = "scottrobinson4591@gmail.com",
        formId = "234322-234563",
        userId = "sr243523",
        searchParameters = "none",
        isUserSettingsSelected = false,
        date = LocalDate.now()
    )
    MainScreen(state = state, onEvent = {})
}