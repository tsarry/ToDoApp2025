package com.example.todoapp2025.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val category: String = "",
    val dueAt: Long? = null,      // epoch millis
    val priority: Int = 3,        // 1 = high … 5 = low
    val completed: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)