package com.example.todoapp.viewmodel

import com.example.todoapp.event.ItemEvent
import com.example.todoapp.shared.FakeItemDao
import com.example.todoapp.shared.SortTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.Calendar
import java.util.Date

class ItemMainViewModelTest {
    private lateinit var viewModel: ItemMainViewModel
    private lateinit var fakeDao: FakeItemDao
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeDao = FakeItemDao()
        viewModel = ItemMainViewModel(fakeDao)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    @DisplayName("SetTitle updates state title")
    fun setTitleUpdatesStateTitle() {
        viewModel.onEvent(ItemEvent.SetTitle("New Title"))

        Assertions.assertEquals("New Title", viewModel.state.value.title)
    }

    @Test
    @DisplayName("SetDescription updates state description")
    fun setDescriptionUpdatesStateDescription() {
        viewModel.onEvent(ItemEvent.SetDescription("New Description"))

        Assertions.assertEquals("New Description", viewModel.state.value.description)
    }

    @Test
    @DisplayName("SetDueDate updates state dueDate")
    fun setDueDateUpdatesStateDueDate() {
        val date = Date()
        viewModel.onEvent(ItemEvent.SetDueDate(date))

        Assertions.assertEquals(date, viewModel.state.value.dueDate)
    }

    @Test
    @DisplayName("ShowDialog sets isAddingItem to true")
    fun showDialogSetsIsAddingItemTrue() {
        viewModel.onEvent(ItemEvent.ShowDialog)

        Assertions.assertTrue(viewModel.state.value.isAddingItem)
    }

    @Test
    @DisplayName("HideDialog sets isAddingItem to false")
    fun hideDialogSetsIsAddingItemFalse() {
        viewModel.onEvent(ItemEvent.ShowDialog)
        viewModel.onEvent(ItemEvent.HideDialog)

        Assertions.assertFalse(viewModel.state.value.isAddingItem)
    }

    @Test
    @DisplayName("HideDialog resets all form fields")
    fun hideDialogResetsAllFormFields() {
        viewModel.onEvent(ItemEvent.SetTitle("Test"))
        viewModel.onEvent(ItemEvent.SetDescription("Desc"))
        viewModel.onEvent(ItemEvent.SetDueDate(Date()))
        viewModel.onEvent(ItemEvent.ShowDialog)
        viewModel.onEvent(ItemEvent.HideDialog)

        Assertions.assertAll(
            { Assertions.assertEquals("", viewModel.state.value.title) },
            { Assertions.assertEquals("", viewModel.state.value.description) },
            { Assertions.assertNull(viewModel.state.value.dueDate) },
            { Assertions.assertFalse(viewModel.state.value.isAddingItem) },
        )
    }

    @Test
    @DisplayName("SortItems updates sortType")
    fun sortItemsUpdatesSortType() {
        viewModel.onEvent(ItemEvent.SortItems(SortTypes.DUE_DATE))

        Assertions.assertEquals(SortTypes.DUE_DATE, viewModel.state.value.sortType)
    }

    @Test
    @DisplayName("getCurrentDate returns date with zero time components")
    fun getCurrentDateReturnsDateWithZeroTimeComponents() {
        val date = viewModel.getCurrentDate()
        val calendar = Calendar.getInstance()
        calendar.time = date

        Assertions.assertAll(
            { Assertions.assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY)) },
            { Assertions.assertEquals(0, calendar.get(Calendar.MINUTE)) },
            { Assertions.assertEquals(0, calendar.get(Calendar.SECOND)) },
            { Assertions.assertEquals(0, calendar.get(Calendar.MILLISECOND)) },
        )
    }

    @Test
    @DisplayName("resetAddForm clears all form fields")
    fun resetAddFormClearsAllFormFields() {
        viewModel.onEvent(ItemEvent.SetTitle("Test"))
        viewModel.onEvent(ItemEvent.SetDescription("Desc"))
        viewModel.onEvent(ItemEvent.SetDueDate(Date()))
        viewModel.onEvent(ItemEvent.ShowDialog)

        viewModel.resetAddForm()

        Assertions.assertAll(
            { Assertions.assertEquals("", viewModel.state.value.title) },
            { Assertions.assertEquals("", viewModel.state.value.description) },
            { Assertions.assertNull(viewModel.state.value.dueDate) },
            { Assertions.assertFalse(viewModel.state.value.isAddingItem) },
        )
    }
}
