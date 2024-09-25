package pl.technoviking.presentation.list.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pl.technoviking.design.R
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun DatePickerSection(
    startDate: Long?,
    endDate: Long?,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
    onFetchButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_arrangement_space))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_arrangement_space))
        ) {
            DatePickerButton(
                modifier = Modifier.weight(1f),
                dateEpoch = startDate,
                placeholderText = stringResource(R.string.start_date),
                onDateClick = onStartDateClick,
            )
            DatePickerButton(
                modifier = Modifier.weight(1f),
                dateEpoch = endDate,
                placeholderText = stringResource(R.string.end_date),
                onDateClick = onEndDateClick,
            )
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onFetchButtonClick,
            enabled = startDate != null && endDate != null
        ) {
            Text(
                text = stringResource(R.string.fetch_data).uppercase(),
                modifier = Modifier.align(Alignment.CenterVertically),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DatePickerButton(
    modifier: Modifier = Modifier,
    dateEpoch: Long?,
    placeholderText: String,
    onDateClick: () -> Unit
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onDateClick,
        shape = OutlinedTextFieldDefaults.shape
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 10.dp),
                style = MaterialTheme.typography.bodyLarge,
                text = dateEpoch?.let { formatDate(dateEpoch) } ?: placeholderText
            )
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null
            )
        }
    }
}

private fun formatDate(dateEpoch: Long): String =
    Instant.ofEpochMilli(dateEpoch).atZone(ZoneId.of("UTC"))
        .format(DateTimeFormatter.ISO_LOCAL_DATE)