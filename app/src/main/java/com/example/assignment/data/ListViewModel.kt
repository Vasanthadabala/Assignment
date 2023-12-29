package com.example.assignment.data

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.assignment.data.roomdb.AppDatabase
import com.example.assignment.data.roomdb.ListDao
import com.example.assignment.data.roomdb.ListEntity
import kotlinx.coroutines.launch

class ListViewModel(application: Application):AndroidViewModel(application){
    private val dao: ListDao

    init {
        val database: AppDatabase =
            Room.databaseBuilder(application, AppDatabase::class.java, "database1")
                .build()
        dao = database.listDao()
    }

    fun getListOrderdByTitle():LiveData<List<ListEntity>>{
        return dao.getListOrderdByTitle()
    }

    fun insertItem(name: String, quantity: Int, rating: Double, remarks: String, images:List<Uri>) {
        val imagePaths = images.map { it.toString() }
        val newItem = ListEntity(name = name, quantity = quantity, rating = rating, remarks = remarks, images =imagePaths)
        viewModelScope.launch {
            dao.insertList(newItem)
        }
    }

    fun saveItem(name: String, quantity: Int, rating: Double, remarks: String,images: List<Uri>) {
        insertItem(name, quantity, rating, remarks, images)
    }

    fun deleteItem(id: Int) {
        viewModelScope.launch {
            dao.deleteListById(id)
        }
    }
    fun getItemById(itemId: Int): LiveData<ListEntity> {
        return dao.getItemById(itemId)
    }
    fun updateItem(id: Int, name: String, quantity: Int, rating: Double, remarks: String,images: List<Uri>) {
        val imagePaths = images.map { it.toString() }
        viewModelScope.launch {
            dao.updateItem(id, name, quantity, rating, remarks,imagePaths)
        }
    }
}