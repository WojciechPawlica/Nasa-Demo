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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
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