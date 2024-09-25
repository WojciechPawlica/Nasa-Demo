@file:OptIn(ExperimentalFoundationApi::class)

package pl.technoviking.presentation.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
internal data object NeoListScreenDest

internal fun NavGraphBuilder.neoListScreen() {
    composable<NeoListScreenDest> {
        NeoListScreen()
    }
}

@Composable
internal fun NeoListScreen(viewModel: NeoListViewModel = hiltViewModel()) {
    NeoListContent(state = viewModel.state, action = viewModel::handleAction)
}

@Composable
private fun NeoListContent(state: NeoListUiState, action: (NeoListAction) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        state.neos.forEach { entry ->
            stickyHeader(key = entry.key) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Gray),
                ) {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = entry.key,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
            items(items = entry.value, key = { it.id }) { item ->
                Card {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Name: ${item.name}")
                        Text("${item.diameterMinKm}-${item.diameterMaxKm}")
                        Text("Is dangerous: ${item.isDangerous}")
                    }
                }
            }
        }
    }
}
