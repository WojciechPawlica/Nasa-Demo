@file:OptIn(ExperimentalTestApi::class)

package pl.technoviking.presentation.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.technoviking.common.testing.HiltTestActivity
import pl.technoviking.design.theme.NasaDemoAppTheme
import pl.technoviking.presentation.list.composable.TAG_CONFIRM_BUTTON
import pl.technoviking.presentation.list.composable.TAG_DATEPICKER
import pl.technoviking.presentation.list.composable.TAG_DATEPICKER_DIALOG
import pl.technoviking.presentation.list.composable.TAG_DISMISS_BUTTON
import pl.technoviking.presentation.list.composable.TAG_END_DATE_BUTTON
import pl.technoviking.presentation.list.composable.TAG_FETCH_BUTTON
import pl.technoviking.presentation.list.composable.TAG_START_DATE_BUTTON
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@HiltAndroidTest
class NeoListScreenTest {
    @get:Rule(order = 1)
    val hiltTestRule = HiltAndroidRule(this)

    @get: Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    private val resources = InstrumentationRegistry.getInstrumentation().targetContext.resources

    @Before
    fun setup() {
        hiltTestRule.inject()
        composeTestRule.setContent {
            NasaDemoAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        NeoListScreen(navToDetails = {})
                    }
                }
            }
        }
    }

    @Test
    fun testNeoListScreen() {
        with(composeTestRule) {
            // Should be empty list
            onNodeWithTag(TAG_NOTHING_TO_SHOW)
                .assertExists()

            // Fetch button should be disabled
            onNodeWithTag(TAG_FETCH_BUTTON)
                .assertExists()
                .assertIsNotEnabled()

            // Show startDate dialog
            onNodeWithTag(TAG_START_DATE_BUTTON)
                .assertExists()
                .performClick()

            // Dismiss dialog
            waitUntilExactlyOneExists(hasTestTag(TAG_DATEPICKER_DIALOG))
            onNodeWithTag(TAG_DISMISS_BUTTON)
                .assertExists()
                .performClick()
            waitUntilDoesNotExist(hasTestTag(TAG_DATEPICKER_DIALOG))

            // Show startDate dialog and pick date
            onNodeWithTag(TAG_START_DATE_BUTTON)
                .assertExists()
                .performClick()
            waitUntilExactlyOneExists(hasTestTag(TAG_DATEPICKER_DIALOG))
            onNodeWithTag(TAG_DATEPICKER).onChildAt(3).onChildAt(10).onChildren()
                .filterToOne(hasText(getTodayFormattedStringForDatePicker()))
                .performClick()
            onNodeWithTag(TAG_CONFIRM_BUTTON)
                .assertExists()
                .performClick()
            waitUntilDoesNotExist(hasTestTag(TAG_DATEPICKER_DIALOG))

            // Show endDate dialog and pick date
            onNodeWithTag(TAG_END_DATE_BUTTON)
                .assertExists()
                .performClick()
            waitUntilExactlyOneExists(hasTestTag(TAG_DATEPICKER_DIALOG))
            onNodeWithTag(TAG_DATEPICKER).onChildAt(3).onChildAt(10).onChildren()
                .filterToOne(hasText(getTodayFormattedStringForDatePicker()))
                .performClick()
            onNodeWithTag(TAG_CONFIRM_BUTTON)
                .assertExists()
                .performClick()
            waitUntilDoesNotExist(hasTestTag(TAG_DATEPICKER_DIALOG))

            // Fetch button should be enabled, then click
            onNodeWithTag(TAG_FETCH_BUTTON)
                .assertExists()
                .assertIsEnabled()
                .performClick()

            // Show list
            waitUntilExactlyOneExists(hasTestTag(TAG_PROGRESS_INDICATOR))
            waitUntilExactlyOneExists(hasTestTag(TAG_LIST))
        }
    }

    private fun getTodayFormattedStringForDatePicker() =
        "Today, " + LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"))
}