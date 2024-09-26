package pl.technoviking.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import pl.technoviking.presentation.details.NeoDetailsScreenDest
import pl.technoviking.presentation.details.neoDetailsScreen
import pl.technoviking.presentation.list.NeoListScreenDest
import pl.technoviking.presentation.list.neoListScreen

@Serializable
data object NeoGraphDest

fun NavGraphBuilder.neoGraph(navController: NavController) {
    navigation<NeoGraphDest>(startDestination = NeoListScreenDest) {
        neoListScreen { id -> navController.navigate(NeoDetailsScreenDest(id)) }
        neoDetailsScreen()
    }
}