package com.example.todoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.dao.ItemDao
import com.example.todoapp.entity.Item
import com.example.todoapp.event.ItemEvent
import com.example.todoapp.shared.ItemState
import com.example.todoapp.shared.SortTypes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class ItemMainViewModel(
    private val itemDao: ItemDao
) : ViewModel() {

    private val _state = MutableStateFlow(ItemState())
    val state: StateFlow<ItemState> = _state.asStateFlow()

    init {
        loadItems()
    }

    private fun loadItems() {
        viewModelScope.launch {
            itemDao.getItemsByTitle().collect { items ->
                _state.update { it.copy(items = items) }
            }
        }
    }

    private fun reloadItems() {
        viewModelScope.launch {
            val flow = when (_state.value.sortType) {
                SortTypes.TITLE -> itemDao.getItemsByTitle()
                SortTypes.CREATED_ON -> itemDao.getItemsByCreatedOn()
                SortTypes.DUE_DATE -> itemDao.getItemsByDueDate()
            }
            flow.collect { items ->
                _state.update { it.copy(items = items) }
            }
        }
    }

    fun onEvent(event: ItemEvent) {
        when (event) {
            is ItemEvent.DeleteItem -> {
                viewModelScope.launch {
                    itemDao.deleteItem(event.item)
                }
            }
            ItemEvent.HideDialog -> {
                resetAddForm()
            }
            ItemEvent.SaveItem -> {
                val item = Item(
                    title = _state.value.title,
                    description = _state.value.description,
                    dueDate = _state.value.dueDate,
                    createdOn = getCurrentDate()
                )
                viewModelScope.launch {
                    itemDao.insertTodo(item)
                    resetAddForm()
                }
            }
            is ItemEvent.UpdateItem -> {
                viewModelScope.launch {
                    itemDao.updateTodo(event.item)
                }
            }
            is ItemEvent.SetDescription -> {
                _state.update { it.copy(description = event.description) }
            }
            is ItemEvent.SetDueDate -> {
                _state.update { it.copy(dueDate = event.dueDate) }
            }
            is ItemEvent.SetTitle -> {
                _state.update { it.copy(title = event.title) }
            }
            ItemEvent.ShowDialog -> {
                _state.update { it.copy(isAddingItem = true) }
            }
            is ItemEvent.SortItems -> {
                _state.update { it.copy(sortType = event.sortType) }
                reloadItems()
            }
        }
    }

    fun resetAddForm() {
        _state.update {
            it.copy(
                isAddingItem = false,
                title = "",
                description = "",
                dueDate = null,
                createdOn = null
            )
        }
    }

    fun getCurrentDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }
}