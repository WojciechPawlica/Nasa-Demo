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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
        modifier = Modifier.testTag(TAG_DATEPICKER_DIALOG),
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                modifier = Modifier.testTag(TAG_CONFIRM_BUTTON),
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis ?: return@TextButton)
                }) {
                Text(stringResource(R.string.ok).uppercase())
            }
        },
        dismissButton = {
            TextButton(
                modifier = Modifier.testTag(TAG_DISMISS_BUTTON),
                onClick = onDismiss
            ) {
                Text(stringResource(R.string.cancel).uppercase())
            }
        }
    ) {
        DatePicker(modifier = Modifier.testTag(TAG_DATEPICKER), state = datePickerState)
    }
}

@Preview
@Composable
fun DatePickerModalPreview() {
    DatePickerModal(null, {}, {})
}

internal const val TAG_DATEPICKER_DIALOG = "tag_datepicker_dialog"
internal const val TAG_DATEPICKER = "tag_datepicker"
internal const val TAG_CONFIRM_BUTTON = "tag_confirm_button"
internal const val TAG_DISMISS_BUTTON = "tag_dismiss_button"