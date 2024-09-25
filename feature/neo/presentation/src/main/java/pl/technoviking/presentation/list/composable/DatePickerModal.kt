@file:OptIn(ExperimentalMaterial3Api::class)

package pl.technoviking.presentation.list.composable

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import pl.technoviking.design.R
import java.time.LocalDate

@Composable
fun DatePickerModal(
    initialDate: Long?,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState =
        rememberDatePickerState(
            initialSelectedDateMillis = initialDate,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= System.currentTimeMillis()
                }

                override fun isSelectableYear(year: Int): Boolean {
                    return year <= LocalDate.now().year
                }
            })

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis ?: return@TextButton)
            }) {
                Text(stringResource(R.string.ok).uppercase())
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel).uppercase())
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}