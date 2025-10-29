package com.example.todoapp2025.vm

enum class SortBy { TITLE, CATEGORY, DUE_AT, PRIORITY, CREATED_AT }

data class SortSpec(
    val by: SortBy = SortBy.CREATED_AT,
    val ascending: Boolean = true
)