package com.example.todoapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todoapp.entity.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun insertTodo(item: Item): Long

    @Update
    suspend fun updateTodo(item: Item)

    @Delete
    suspend fun deleteItem(item: Item)

    @Query("DELETE FROM item WHERE id = :itemId")
    suspend fun deleteById(itemId: Int)

    @Query("SELECT * FROM item WHERE id = :id")
    fun getItemById(id: Int): Flow<Item>

    @Query("SELECT * FROM item ORDER BY title ASC")
    fun getItemsByTitle(): Flow<List<Item>>

    @Query("SELECT * FROM item ORDER BY createdOn ASC")
    fun getItemsByCreatedOn(): Flow<List<Item>>

    @Query("SELECT * FROM item ORDER BY dueDate ASC")
    fun getItemsByDueDate(): Flow<List<Item>>
}
