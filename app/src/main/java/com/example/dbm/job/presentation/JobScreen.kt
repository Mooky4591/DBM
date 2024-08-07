package com.example.dbm.job.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.dbm.main.presentation.CreateTopBar
import com.example.dbm.main.presentation.DisplayUnfinishedProject
import com.example.dbm.main.presentation.NoUnfinishedJobsText

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
                Column {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
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
                            }
                        )
                    }
                }
                Box (modifier = Modifier.fillMaxSize()) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
                        shape = AbsoluteRoundedCornerShape(60.dp, 60.dp, 900.dp, 0.dp)
                    ) {
                        val scrollState = rememberScrollState()
                        LazyColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top,
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(30.dp)
                        ) {

                        }
                    }
                }
            }
        }
    )
}