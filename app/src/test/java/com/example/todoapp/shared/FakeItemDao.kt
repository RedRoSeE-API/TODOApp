package com.example.todoapp.shared

import com.example.todoapp.dao.ItemDao
import com.example.todoapp.entity.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeItemDao : ItemDao {

    private val items = mutableListOf<Item>()
    private var nextId = 1L

    override fun getItemsByTitle(): Flow<List<Item>> = flowOf(items.sortedBy { it.title })

    override fun getItemsByCreatedOn(): Flow<List<Item>> = flowOf(items.sortedBy { it.createdOn })

    override fun getItemsByDueDate(): Flow<List<Item>> = flowOf(items.sortedBy { it.dueDate })

    override fun getItemById(id: Int): Flow<Item> = flowOf(items.first { it.id == id })

    override suspend fun insertTodo(item: Item): Long {
        items.add(item)
        return nextId++
    }

    override suspend fun updateTodo(item: Item) {
        val index = items.indexOfFirst { it.id == item.id }
        if (index != -1) items[index] = item
    }

    override suspend fun deleteItem(item: Item) {
        items.remove(item)
    }

    override suspend fun deleteById(itemId: Int) {
        items.removeIf { it.id == itemId }
    }
}