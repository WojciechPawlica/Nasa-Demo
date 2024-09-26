package pl.technoviking.presentation.details

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import pl.technoviking.common.di.MainDispatcher
import pl.technoviking.data.NeoRepository
import pl.technoviking.data.model.NeoExtended
import javax.inject.Inject

data class NeoDetailsUiState(
    val neoExtended: NeoExtended? = null
)

@HiltViewModel
class NeoDetailsViewModel @Inject constructor(
    private val repository: NeoRepository,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state by mutableStateOf(NeoDetailsUiState())
        private set

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(NeoDetailsViewModel::class.simpleName, throwable.message, throwable)
    }

    init {
        viewModelScope.launch(mainDispatcher + exceptionHandler) {
            val id = savedStateHandle.get<String>("id")
                ?: throw IllegalArgumentException("Id not provided!")
            state = state.copy(neoExtended = repository.getNeoExtended(id))
        }
    }
}