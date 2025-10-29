package com.example.todoapp2025.vm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp2025.data.AppDatabase
import com.example.todoapp2025.data.TodoRepository

class TodoVMFactory(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = AppDatabase.get(app).todoDao()
        val repo = TodoRepository(dao)
        @Suppress("UNCHECKED_CAST")
        return TodoViewModel(repo) as T
    }
}