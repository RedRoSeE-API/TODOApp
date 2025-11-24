package com.example.todoapp.shared

import com.example.todoapp.entity.Item
import java.util.Date

data class ItemState(
    val items: List<Item> = emptyList(),
    val title: String = "",
    val description: String = "",
    val dueDate: Date? = null,
    val createdOn: Date? = null,
    val isAddingItem: Boolean = false,
    val sortType: SortTypes = SortTypes.TITLE,
)
