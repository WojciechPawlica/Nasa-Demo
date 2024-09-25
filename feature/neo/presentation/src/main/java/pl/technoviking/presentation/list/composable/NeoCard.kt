package pl.technoviking.presentation.list.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pl.technoviking.data.model.NeoSimple
import pl.technoviking.design.R

@Composable
fun NeoCard(modifier: Modifier = Modifier, item: NeoSimple, onCardClick: (String) -> Unit) {
    Card(modifier = modifier, onClick = { onCardClick(item.id) }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_arrangement_space))
        ) {
            Column {
                Text(stringResource(R.string.name), style = MaterialTheme.typography.titleMedium)
                Text(item.name, style = MaterialTheme.typography.bodyMedium)
            }
            Column {
                Text(
                    stringResource(R.string.diameter),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "${String.format("%.2f", item.diameterMinKm)} - ${String.format("%.2f", item.diameterMaxKm)} [km]",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (item.isDangerous) {
                Text(
                    stringResource(R.string.dangerous),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFDD0000)
                )
            } else {
                Text(
                    stringResource(R.string.not_dangerous),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF00DD00)
                )
            }
        }
    }
}