package pl.technoviking.presentation.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import pl.technoviking.data.model.EstimatedDiameter
import pl.technoviking.data.model.EstimatedDiameterValues
import pl.technoviking.data.model.NeoExtended
import pl.technoviking.design.R

@Serializable
internal data class NeoDetailsScreenDest(val id: String)

internal fun NavGraphBuilder.neoDetailsScreen() {
    composable<NeoDetailsScreenDest> {
        NeoDetailsScreen()
    }
}

@Composable
internal fun NeoDetailsScreen(viewModel: NeoDetailsViewModel = hiltViewModel()) {
    NeoDetailsContent(state = viewModel.state)
}

@Composable
private fun NeoDetailsContent(state: NeoDetailsUiState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.default_horizontal_padding))

            .verticalScroll(rememberScrollState())
    ) {
        Text(text = state.neoExtended.prettyPrint())
    }
}

@Preview(showBackground = true)
@Composable
fun NeoDetailsContentPreview() {
    NeoDetailsContent(
        state = NeoDetailsUiState(
            NeoExtended(
                id = "1",
                name = "Asteroid",
                absoluteMagnitudeH = 198.199,
                estimatedDiameter = EstimatedDiameter(
                    kilometers = EstimatedDiameterValues(
                        estimatedDiameterMin = 200.201,
                        estimatedDiameterMax = 202.203
                    ), meters = EstimatedDiameterValues(
                        estimatedDiameterMin = 204.205,
                        estimatedDiameterMax = 206.207
                    ), miles = EstimatedDiameterValues(
                        estimatedDiameterMin = 208.209,
                        estimatedDiameterMax = 210.211
                    ), feet = EstimatedDiameterValues(
                        estimatedDiameterMin = 212.213,
                        estimatedDiameterMax = 214.215
                    )
                ),
                isPotentiallyHazardousAsteroid = false,
                closeApproachData = listOf(),
                isSentryObject = false
            )
        )
    )
}

/**
 * This is not my code, just a tool to quickly format content without having to create a complex view.
 */
private fun Any?.prettyPrint(): String {
    var indentLevel = 0
    val indentWidth = 4

    fun padding() = "".padStart(indentLevel * indentWidth)

    val toString = toString()

    val stringBuilder = StringBuilder(toString.length)

    var i = 0
    while (i < toString.length) {
        when (val char = toString[i]) {
            '(', '[', '{' -> {
                indentLevel++
                stringBuilder.appendLine(char).append(padding())
            }

            ')', ']', '}' -> {
                indentLevel--
                stringBuilder.appendLine().append(padding()).append(char)
            }

            ',' -> {
                stringBuilder.appendLine(char).append(padding())
                // ignore space after comma as we have added a newline
                val nextChar = toString.getOrElse(i + 1) { char }
                if (nextChar == ' ') i++
            }

            else -> {
                stringBuilder.append(char)
            }
        }
        i++
    }
    return stringBuilder.toString()
}