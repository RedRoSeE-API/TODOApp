package com.example.todoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.dao.ItemDao
import com.example.todoapp.entity.Item
import com.example.todoapp.event.ItemEvent
import com.example.todoapp.shared.ItemState
import com.example.todoapp.shared.SortTypes
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class ItemMainViewModel(
    private val itemDao: ItemDao
): ViewModel() {

    private val _sortType = MutableStateFlow(SortTypes.TITLE)

    private val _items = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                SortTypes.TITLE -> itemDao.getItemsByTitle()
                SortTypes.CREATED_ON -> itemDao.getItemsByCreatedOn()
                SortTypes.DUE_DATE -> itemDao.getItemsByDueDate()
            }
        }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(ItemState())
    val state = combine(_state, _sortType, _items) {
        state, sortType, items ->
        state.copy(
            items = items,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ItemState())


    fun onEvent(event: ItemEvent) {
        when(event){
            is ItemEvent.DeleteItem -> {
                viewModelScope.launch {
                    itemDao.deleteItem(event.item)
                }
            }
            ItemEvent.HideDialog -> {
                resetAddForm()
            }
            ItemEvent.SaveItem -> {
                val title = state.value.title
                val description = state.value.description
                val dueDate = state.value.dueDate

                val item = Item(
                    title = title,
                    description = description,
                    dueDate = dueDate,
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
                _state.update {
                    it.copy(
                        description = event.description
                    )
                }
            }
            is ItemEvent.SetDueDate -> {
                _state.update {
                    it.copy(
                        dueDate = event.dueDate
                    )
                }
            }
            is ItemEvent.SetTitle -> {
                _state.update {
                    it.copy(
                        title = event.title
                    )
                }
            }
            ItemEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingItem = true
                    )
                }
            }
            is ItemEvent.SortItems -> {
                _sortType.value = event.sortType
            }
        }
    }

    fun resetAddForm(){
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