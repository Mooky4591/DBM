package com.example.dbm.main.presentation

import android.widget.Space
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
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dbm.R
import com.example.dbm.presentation.theme.DbmPurple
import com.example.dbm.presentation.theme.GradientDarkPurple
import com.example.dbm.presentation.theme.GradientPink

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
                        action = onEvent,
                        initials = state.initials ?: "",
                        name = state.name ?: "",
                        userId = state.userId ?: ""
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
                    Spacer(modifier = Modifier.width(15.dp))
                    CreateSquareIconButton(
                        icon = ImageVector.vectorResource(id = R.drawable.history), 
                        contentDescription = "history",
                        onClick = { onEvent(MainEvents.StartNewProject) }
                        )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                shape = AbsoluteRoundedCornerShape(60.dp, 60.dp, 900.dp, 0.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxHeight(.75f)
                        .padding(30.dp)
                ) {

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
                    text = "Start New Job",
                    width = 300.dp,
                    image = R.drawable.plus
                )
            }
        }
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
        shape = RoundedCornerShape(10.dp),
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
        action: (MainEvents) -> Unit,
        initials: String,
        userId: String,
        name: String,
    ) {
        IconButton(
            onClick = {
                /*TODO*/
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
        CircleWithInitials(
            onItemSelected = action,
            initials = initials,
            userId = userId
        )
    }

    @Composable
    fun CircleWithInitials(
        onItemSelected: (MainEvents) -> Unit,
        initials: String,
        userId: String
    ) {
        Surface(
            shape = CircleShape,
            color = Color.LightGray,
            modifier =
            Modifier
                .size(40.dp)
                .clickable {
                    onItemSelected(MainEvents.OnUserSettingsSelected(userId))
                },
        ) {
            Box(
                modifier =
                Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = initials,
                    fontSize = 16.sp,
                    lineHeight = 19.sp,
                    fontWeight = FontWeight.W700,
                    color = Color.Gray
                )
            }
        }
    }

