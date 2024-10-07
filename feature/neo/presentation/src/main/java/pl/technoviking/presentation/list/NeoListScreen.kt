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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import pl.technoviking.data.model.NeoSimple
import pl.technoviking.design.R
import pl.technoviking.presentation.list.NeoListEvent.OnCardClicked
import pl.technoviking.presentation.list.NeoListEvent.OnConfirmDateButtonClicked
import pl.technoviking.presentation.list.NeoListEvent.OnDismissDatePickerButtonClicked
import pl.technoviking.presentation.list.NeoListEvent.OnFetchDataButtonClicked
import pl.technoviking.presentation.list.NeoListEvent.OnShowDatePickerButtonClicked
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
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is NeoListEffect.NavToCardDetails -> navToDetails(effect.id)
            }
        }
    }
    NeoListContent(state = viewModel.state, action = { viewModel.handleAction(it) })
}

@Composable
private fun NeoListContent(state: NeoListUiState, action: (NeoListEvent) -> Unit) {
    state.datePicker?.let { datePicker ->
        DatePickerModal(
            initialDate = when (datePicker) {
                DatePicker.START_DATE -> state.startDate
                DatePicker.END_DATE -> state.endDate
            },
            onDateSelected = { action(OnConfirmDateButtonClicked(datePicker, it)) },
            onDismiss = { action(OnDismissDatePickerButtonClicked) })
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
                            .testTag(TAG_PROGRESS_INDICATOR)
                    )
                }

                state.neos.isEmpty() -> {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .testTag(TAG_NOTHING_TO_SHOW),
                        text = stringResource(id = R.string.nothing_to_show),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag(TAG_LIST),
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
                                NeoCard(item = item) { action(OnCardClicked(item.id)) }
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
            onStartDateClick = { action(OnShowDatePickerButtonClicked(DatePicker.START_DATE)) },
            onEndDateClick = { action(OnShowDatePickerButtonClicked(DatePicker.END_DATE)) },
            onFetchButtonClick = { action(OnFetchDataButtonClicked) })
    }
}

@Preview(showBackground = true)
@Composable
fun NeoListContentPreview() {
    NeoListContent(
        state = NeoListUiState(
            neos = mapOf(
                "1970-01-01" to listOf(
                    NeoSimple(
                        id = "1",
                        name = "Asteroid",
                        diameterMinKm = 176.177,
                        diameterMaxKm = 178.179,
                        isDangerous = false
                    ),
                    NeoSimple(
                        id = "2",
                        name = "Comet",
                        diameterMinKm = 176.177,
                        diameterMaxKm = 178.179,
                        isDangerous = false
                    ),
                    NeoSimple(
                        id = "3",
                        name = "Moon",
                        diameterMinKm = 176.177,
                        diameterMaxKm = 178.179,
                        isDangerous = true
                    ),
                    NeoSimple(
                        id = "4",
                        name = "Asteroid",
                        diameterMinKm = 176.177,
                        diameterMaxKm = 178.179,
                        isDangerous = false
                    ),
                    NeoSimple(
                        id = "5",
                        name = "Comet",
                        diameterMinKm = 176.177,
                        diameterMaxKm = 178.179,
                        isDangerous = false
                    ),
                )
            ),
            isLoading = false,
            errorMessage = null,
            datePicker = null,
            startDate = 0L,
            endDate = null

        )
    ) {}
}

internal const val TAG_NOTHING_TO_SHOW = "tag_nothing_to_show"
internal const val TAG_PROGRESS_INDICATOR = "tag_progress_indicator"
internal const val TAG_LIST = "tag_list"