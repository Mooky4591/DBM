package com.example.dbm.job_search.presentation

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dbm.R
import com.example.dbm.main.presentation.CreateTopBar
import com.example.dbm.main.presentation.DisplayProject
import com.example.dbm.main.presentation.MainEvents

@Composable
fun SearchScreen (
    state: SearchState,
    onEvents: (SearchEvents) -> Unit
) {
    val viewModel: SearchViewModel = hiltViewModel()
    val jobs by viewModel.jobs.collectAsState(initial = emptyList())

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
                        backPressed = { onEvents(SearchEvents.OnBackPressed) },
                        name = state.name ?: "",
                        isUserSettingsDropDownExpanded = state.isUserSettingsDropDownExpanded,
                        userSettingsPressed = { onEvents(SearchEvents.OnUserSettingsPressed)},
                        shouldShowSettingsButton = true,
                        shouldShowSaveButton = false,
                        action = { event ->
                            when (event) {
                                MainEvents.OnAccountSettingsPressed -> {
                                    onEvents(SearchEvents.OnAccountSettingsPressed)
                                }
                                MainEvents.OnLogoutPressed -> {
                                    onEvents(SearchEvents.OnLogoutPressed)
                                }
                                MainEvents.OnContactUsPressed -> {
                                    onEvents(SearchEvents.OnContactUsPressed)
                                }
                                else -> {}
                            }
                        }
                    )
                }
                CreateSearchBar(
                    value = state.searchText ?: "",
                    placeholderText = "Start typing to filter",
                    onTextChange = { text ->
                        onEvents(SearchEvents.OnSearchTextChange(text))
                    },
                )
                Box (modifier = Modifier.fillMaxSize()) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
                    ) {
                        LazyColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top,
                            modifier = Modifier
                                .fillMaxHeight(.75f)
                                .padding(10.dp)
                        ) {
                            items(jobs) { job ->
                                DisplayProject(
                                    job = job,
                                    onClick = { onEvents(SearchEvents.OnJobSelected(job.formId ?: "")) },
                                    selectedJobId = state.selectedJobId ?: "",
                                    action = null,
                                    onElipsisSelected = null
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun CreateSearchBar(
    value: String,
    onTextChange: (String) -> Unit,
    placeholderText: String,
) {
    TextField(
        value = value,
        onValueChange = {
            onTextChange(it)
        },
        placeholder = {
            Text(
                text = placeholderText
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 10.dp),
        shape = RoundedCornerShape(50.dp),
        trailingIcon = {
            Icon(painter = painterResource(id = R.drawable.search), contentDescription = "Search", modifier = Modifier.padding(end = 8.dp))
        }
    )
}

@Preview
@Composable
fun SearchScreenPreview(){
    val state = SearchState(
        email = "scottrobinson4591@gmail.com",
        name = "Scott Robinson",
        jobId = "sjkh2345",
        userId = "aldfkjgh"
    )

    SearchScreen(state = state) {
        
    }
}