package com.example.pr4ma

data class Plant(
    val id: Long = 0,  // SQLite будет автоматически генерировать ID через AUTOINCREMENT
    val name: String,
    val description: String
)
