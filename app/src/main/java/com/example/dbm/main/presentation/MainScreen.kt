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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dbm.R
import com.example.dbm.main.presentation.objects.Forms
import com.example.dbm.presentation.theme.DbmPurple
import com.example.dbm.presentation.theme.GradientDarkPurple
import com.example.dbm.presentation.theme.GradientPink
import com.example.dbm.presentation.theme.MellowYellow
import java.time.LocalDate

@Composable
fun MainScreen (
    state: MainState,
    onEvent: (MainEvents) -> Unit
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
                    horizontalArrangement = Arrangement.SpaceAround,
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
                        }
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
                val unsubmittedProjectsList = (0..state.unsubmittedProjects.size).toList()
                if (unsubmittedProjectsList.size > 1) {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier
                            .fillMaxHeight(.75f)
                            .padding(30.dp)
                    ) {
                        items(
                            items = state.unsubmittedProjects,
                            key = { item -> item.formId }) { item ->
                            Box {
                                DisplayUnfinishedProject(
                                    address = item.jobAddress,
                                    dateCreated = item.dateCreated,
                                    projectId = item.formId
                                )
                            }
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
fun DisplayUnfinishedProject(
    address: String,
    dateCreated: LocalDate,
    projectId: String
) {
    Card(
        colors = CardDefaults.cardColors(MellowYellow),
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .wrapContentSize()
            .clickable {

            }
            .padding(5.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(8.dp),
                clip = false
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Image(painter = painterResource(id = R.drawable.alert), contentDescription = stringResource(
                R.string.high_priority
            )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = address)
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "Date Created: $dateCreated")
            }
        }

    }

}

@Composable
fun NoUnfinishedJobsText() {
    Text(text = stringResource(R.string.no_unfinished_jobs), color = Color.Black, fontSize = 15.sp)
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
        userSettingsPressed: (Boolean) -> Unit,
        isUserSettingsDropDownExpanded: Boolean,
        name: String,
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
        Text(
            text = name,
            fontSize = 30.sp,
            color = Color.White
        )
        Surface(
            color = Color.Transparent
        ) {
            IconButton(
                onClick = {
                    userSettingsPressed(!isUserSettingsDropDownExpanded)
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.account_icon),
                    contentDescription = "account"
                )
            }
            CreateDropDownMenu(
                expanded = isUserSettingsDropDownExpanded,
                dismiss = {
                    userSettingsPressed(!isUserSettingsDropDownExpanded)
                },
                menuColor = DbmPurple,
                menuItemColor = Color.LightGray
            )
        }
    }

@Composable
fun CreateDropDownMenu(
    expanded: Boolean,
    dismiss: (Boolean) -> Unit,
    menuColor: Color,
    menuItemColor: Color
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { dismiss(!expanded) },
        modifier = Modifier.background(menuColor)
    ) {
        Divider(thickness = .5.dp, color = Color.Black)
        DropdownMenuItem(
            text = {
                DropdownMenuItemText(itemTitle = stringResource(R.string.account_settings))
            },
            onClick = {
                /*TODO*/
            },
            modifier = Modifier.background(color = menuItemColor)
        )
        Divider(thickness = .5.dp, color = Color.Black)
        DropdownMenuItem(
            text = {
                DropdownMenuItemText(itemTitle = stringResource(R.string.contact_us))
            },
            onClick = {
                /*TODO*/
            },
            modifier = Modifier.background(color = menuItemColor)
        )
        Divider(thickness = .5.dp, color = Color.Black)
        DropdownMenuItem(
            text = {
                DropdownMenuItemText(itemTitle = stringResource(R.string.logout))
            },
            onClick = {
                /*TODO*/
            },
            modifier = Modifier.background(color = menuItemColor)
        )
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
        unsubmittedProjects = listOf(
            Forms(
                formId = "123",
                dateCreated = LocalDate.now().plusDays(4),
                createdBy = "Scott Robinson",
                jobAddress = "3171 Jessica Drive Douglasville, GA 30135"
            ),
            Forms(
                formId = "234",
                dateCreated = LocalDate.now().plusDays(2),
                createdBy = "Chase Daily",
                jobAddress = "32 Wallaby Way Sydney, Australia"
            )
        ),
        date = LocalDate.now()
    )
    MainScreen(state = state, onEvent = {})
}