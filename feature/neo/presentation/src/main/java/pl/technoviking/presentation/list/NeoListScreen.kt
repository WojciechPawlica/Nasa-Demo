@file:OptIn(ExperimentalFoundationApi::class)

package pl.technoviking.presentation.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import pl.technoviking.design.R
import pl.technoviking.presentation.list.NeoListAction.UiAction
import pl.technoviking.presentation.list.NeoListAction.UiAction.CardClicked
import pl.technoviking.presentation.list.NeoListAction.VmAction
import pl.technoviking.presentation.list.NeoListAction.VmAction.DatePicked
import pl.technoviking.presentation.list.NeoListAction.VmAction.DismissDatePicker
import pl.technoviking.presentation.list.NeoListAction.VmAction.FetchData
import pl.technoviking.presentation.list.NeoListAction.VmAction.ShowDatePicker
import pl.technoviking.presentation.list.composable.DatePickerModal
import pl.technoviking.presentation.list.composable.DatePickerSection
import pl.technoviking.presentation.list.composable.NeoCard

@Serializable
internal data object NeoListScreenDest

internal fun NavGraphBuilder.neoListScreen(navToDetails: (String) -> Unit) {
    composable<NeoListScreenDest> {
        NeoListScreen(navToDetails = navToDetails)
    }
}

@Composable
internal fun NeoListScreen(
    viewModel: NeoListViewModel = hiltViewModel(),
    navToDetails: (String) -> Unit
) {
    NeoListContent(state = viewModel.state, action = { action ->
        when (action) {
            is VmAction -> viewModel.handleAction(action)
            is UiAction -> when (action) {
                is CardClicked -> navToDetails(action.id)
            }
        }
    })
}

@Composable
private fun NeoListContent(state: NeoListUiState, action: (NeoListAction) -> Unit) {
    state.datePicker?.let { datePicker ->
        DatePickerModal(
            initialDate = when (datePicker) {
                DatePicker.START_DATE -> state.startDate
                DatePicker.END_DATE -> state.endDate
            },
            onDateSelected = { action(DatePicked(datePicker, it)) },
            onDismiss = { action(DismissDatePicker) })
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.default_horizontal_padding)),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when {
                state.errorMessage != null -> {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = state.errorMessage,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.progress_indicator_size))
                            .align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_arrangement_space))
                    ) {
                        state.neos.forEach { entry ->
                            stickyHeader(key = entry.key) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.Gray)
                                ) {
                                    Text(
                                        modifier = Modifier.padding(10.dp),
                                        text = entry.key,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                            }
                            items(items = entry.value, key = { it.id }) { item ->
                                NeoCard(item = item) { action(CardClicked(item.id)) }
                            }
                        }
                    }
                }
            }
        }
        DatePickerSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            startDate = state.startDate,
            endDate = state.endDate,
            onStartDateClick = { action(ShowDatePicker(DatePicker.START_DATE)) },
            onEndDateClick = { action(ShowDatePicker(DatePicker.END_DATE)) },
            onFetchButtonClick = { action(FetchData) })
    }
}