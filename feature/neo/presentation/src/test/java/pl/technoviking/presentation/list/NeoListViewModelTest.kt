@file:OptIn(ExperimentalCoroutinesApi::class)

package pl.technoviking.presentation.list

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Test
import pl.technoviking.data.NeoRepository

class NeoListViewModelTest {

    private val repository = mockk<NeoRepository>()
    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: NeoListViewModel

    @Before
    fun setup() {
        every { repository.observeNeoSimple() } returns flowOf()
        coEvery { repository.fetchData(any(), any()) } returns Unit
        viewModel = NeoListViewModel(repository, dispatcher, dispatcher)
    }

    @Test
    fun `Test calling getNeoExtended at initialization`() {
        verify(exactly = 1) { repository.observeNeoSimple() }
    }

    @Test
    fun `Test show datepicker dialog`() {
        viewModel.handleAction(NeoListAction.VmAction.ShowDatePicker(DatePicker.START_DATE))
        assert(viewModel.state.datePicker == DatePicker.START_DATE)

        viewModel.handleAction(NeoListAction.VmAction.ShowDatePicker(DatePicker.END_DATE))
        assert(viewModel.state.datePicker == DatePicker.END_DATE)
    }

    @Test
    fun `Test dismiss datepicker dialog`() {
        viewModel.handleAction(NeoListAction.VmAction.ShowDatePicker(DatePicker.START_DATE))
        assert(viewModel.state.datePicker == DatePicker.START_DATE)

        viewModel.handleAction(NeoListAction.VmAction.DismissDatePicker)
        assert(viewModel.state.datePicker == null)
    }

    @Test
    fun `Test date picked`() {
        val epoch = System.currentTimeMillis()
        viewModel.handleAction(NeoListAction.VmAction.ShowDatePicker(DatePicker.START_DATE))
        viewModel.handleAction(NeoListAction.VmAction.DatePicked(DatePicker.START_DATE, epoch))

        assert(viewModel.state.startDate == epoch)
        assert(viewModel.state.datePicker == null)
    }

    @Test
    fun `Test fetch data success`() {
        val epoch = System.currentTimeMillis()
        viewModel.handleAction(NeoListAction.VmAction.DatePicked(DatePicker.START_DATE, epoch))
        viewModel.handleAction(NeoListAction.VmAction.DatePicked(DatePicker.END_DATE, epoch))

        viewModel.handleAction(NeoListAction.VmAction.FetchData)

        coVerify(exactly = 1) { repository.fetchData(any(), any()) }
    }

    @Test
    fun `Test fetch data failed`() {
        val message = "Message"
        coEvery { repository.fetchData(any(), any()) } throws Exception(message)
        val epoch = System.currentTimeMillis()
        viewModel.handleAction(NeoListAction.VmAction.DatePicked(DatePicker.START_DATE, epoch))
        viewModel.handleAction(NeoListAction.VmAction.DatePicked(DatePicker.END_DATE, epoch))

        viewModel.handleAction(NeoListAction.VmAction.FetchData)

        coVerify(exactly = 1) { repository.fetchData(any(), any()) }
        assert(viewModel.state.errorMessage == message)
    }
}