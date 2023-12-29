package com.example.assignment.data.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ListDao{
    @Insert
    suspend fun insertList(list: ListEntity)

    @Delete
    suspend fun deleteList(list: ListEntity)

    @Query("Select * FROM list_items ORDER BY name ASC")
    fun getListOrderdByTitle(): LiveData<List<ListEntity>>

    @Query("DELETE FROM list_items WHERE id = :id")
    suspend fun deleteListById(id: Int)

    @Query("SELECT * FROM list_items WHERE id = :itemId")
    fun getItemById(itemId: Int): LiveData<ListEntity>

    @Query("UPDATE list_items SET name = :name, quantity = :quantity, rating = :rating, remarks = :remarks , images = :images WHERE id = :id")
    suspend fun updateItem(id: Int, name: String, quantity: Int, rating: Double, remarks: String,images:List<String>)

}