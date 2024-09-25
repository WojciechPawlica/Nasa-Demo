package pl.technoviking.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import pl.technoviking.presentation.list.NeoListScreenDest
import pl.technoviking.presentation.list.neoListScreen

@Serializable
data object NeoGraphDest

fun NavGraphBuilder.neoGraph() {
    navigation<NeoGraphDest>(startDestination = NeoListScreenDest) {
        neoListScreen()
    }
}