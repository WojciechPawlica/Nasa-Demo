package pl.technoviking.presentation.list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.technoviking.common.di.IoDispatcher
import pl.technoviking.common.di.MainDispatcher
import pl.technoviking.data.NeoRepository
import pl.technoviking.data.model.NeoSimple
import java.time.LocalDate
import javax.inject.Inject

data class NeoListUiState(
    val neos: Map<String, List<NeoSimple>> = emptyMap()
)

sealed interface NeoListAction {

}

@HiltViewModel
class NeoListViewModel @Inject constructor(
    private val repository: NeoRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
) : ViewModel() {

    var state by mutableStateOf(NeoListUiState())
        private set

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        //TODO handle this with UI message if necessary
        Log.e(NeoListViewModel::class.simpleName, throwable.message, throwable)
    }

    init {
        viewModelScope.launch(mainDispatcher + exceptionHandler) {
            repository.fetchData(
                LocalDate.of(2015, 9, 7),
                LocalDate.of(2015, 9, 14)
            )
        }
        viewModelScope.launch(mainDispatcher + exceptionHandler) {
            withContext(ioDispatcher) {
                repository.observeNeoSimple().collect {
                    withContext(mainDispatcher) {
                        state = state.copy(neos = it)
                    }
                }
            }
        }
    }

    fun handleAction(action: NeoListAction) {

    }
}