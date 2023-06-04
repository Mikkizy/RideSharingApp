package com.ukaka.ridesharingapp.presentation.destination

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.ukaka.ridesharingapp.common.Dimensions
import com.ukaka.ridesharingapp.domain.models.AutoCompleteModel
import com.ukaka.ridesharingapp.presentation.Screen
import com.ukaka.ridesharingapp.presentation.shared.TopBar
import com.ukaka.ridesharingapp.ui.theme.RideSharingAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationScreen(
    state: DestinationState,
    autoCompleteList: List<AutoCompleteModel>,
    onEvents: (DestinationEvents) -> Unit,
) {
    val navController = rememberNavController()

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(title = "Enter Destination") {
                navController.navigateUp()
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
                    .padding(Dimensions.pagePadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimensions.oneSpacer)
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = state.destination,
                        onValueChange = { onEvents(DestinationEvents.EnteredDestination(it)) },
                        placeholder = {
                            Text(text = "Where To?")
                        }
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colors.background)
                    ) {
                        items(items = autoCompleteList) { autoCompleteModel ->
                            DestinationItem(destination = autoCompleteModel.address){
                                onEvents(DestinationEvents.SelectedLocation(autoCompleteModel))
                                navController.navigate(
                                    Screen.RideRequestScreen.route
                                        .replace("{destination}", autoCompleteModel.address)
                                )
                            }
                        }
                    }
                }
        }
    }
}

@Composable
fun DestinationItem(
    destination: String,
    onLocationSelected: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onLocationSelected() }
            .background(MaterialTheme.colors.surface)
    ) {
        Text(
            modifier = Modifier
                .padding(Dimensions.pagePadding),
            text = destination,
            overflow = TextOverflow.Ellipsis
        )
        Divider(modifier = Modifier.fillMaxWidth())
    }
}

@Preview
@Composable
fun DestinationScreenPreview() {
    RideSharingAppTheme {
        DestinationScreen(
            state = DestinationState(),
            autoCompleteList = emptyList(),
            onEvents = {}
        )
    }
}