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
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

data class NeoListUiState(
    val neos: Map<String, List<NeoSimple>> = emptyMap(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val datePicker: DatePicker? = null,
    val startDate: Long? = null,
    val endDate: Long? = null,
)

sealed interface NeoListAction {
    sealed interface UiAction : NeoListAction {
        data class CardClicked(val id: String) : UiAction
    }

    sealed interface VmAction : NeoListAction {
        data class ShowDatePicker(val datePicker: DatePicker) : VmAction
        data object DismissDatePicker : VmAction
        data class DatePicked(val datePicker: DatePicker, val millis: Long) : VmAction
        data object FetchData : VmAction
    }
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
        Log.e(NeoListViewModel::class.simpleName, throwable.message, throwable)
    }

    init {
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

    fun handleAction(action: NeoListAction.VmAction) {
        when (action) {
            is NeoListAction.VmAction.ShowDatePicker -> showDatePicker(action.datePicker)
            NeoListAction.VmAction.DismissDatePicker -> dismissDatePicker()
            is NeoListAction.VmAction.DatePicked -> datePicked(action.datePicker, action.millis)
            NeoListAction.VmAction.FetchData -> fetchData()
        }
    }

    private fun showDatePicker(datePicker: DatePicker) {
        state = state.copy(datePicker = datePicker)
    }

    private fun dismissDatePicker() {
        state = state.copy(datePicker = null)
    }

    private fun datePicked(datePicker: DatePicker, epoch: Long) {
        state = when (datePicker) {
            DatePicker.START_DATE -> state.copy(startDate = epoch)
            DatePicker.END_DATE -> state.copy(endDate = epoch)
        }
        dismissDatePicker()
    }

    private fun fetchData() {
        viewModelScope.launch(mainDispatcher + exceptionHandler) {
            runCatching {
                state = state.copy(isLoading = true, errorMessage = null)
                repository.fetchData(
                    state.startDate?.let { getLocalDate(it) }
                        ?: throw IllegalStateException("Start date missing"),
                    state.endDate?.let { getLocalDate(it) }
                        ?: throw IllegalStateException("End date missing"),
                )
            }.onFailure {
                state = state.copy(errorMessage = it.localizedMessage)
            }.also {
                state = state.copy(isLoading = false)
            }
        }
    }

    private fun getLocalDate(epoch: Long): LocalDate =
        Instant.ofEpochMilli(epoch).atZone(ZoneId.of("UTC")).toLocalDate()
}

enum class DatePicker {
    START_DATE, END_DATE
}