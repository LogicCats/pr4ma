package com.example.pr4ma

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class PlantDao(private val databaseHelper: PlantDatabaseHelper) {

    // Метод для вставки растения в базу данных
    fun insertPlant(plant: Plant): Long {
        val db: SQLiteDatabase = databaseHelper.writableDatabase
        val values = ContentValues().apply {
            put(PlantDatabaseHelper.COLUMN_NAME, plant.name)
            put(PlantDatabaseHelper.COLUMN_DESCRIPTION, plant.description)
        }
        return db.insert(PlantDatabaseHelper.TABLE_PLANTS, null, values)
    }

    // Метод для вставки нескольких растений
    fun insertPlants(plants: List<Plant>): List<Long> {
        val db: SQLiteDatabase = databaseHelper.writableDatabase
        val ids = mutableListOf<Long>()
        db.beginTransaction()
        try {
            for (plant in plants) {
                val values = ContentValues().apply {
                    put(PlantDatabaseHelper.COLUMN_NAME, plant.name)
                    put(PlantDatabaseHelper.COLUMN_DESCRIPTION, plant.description)
                }
                ids.add(db.insert(PlantDatabaseHelper.TABLE_PLANTS, null, values))
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
        return ids
    }

    // Метод для получения всех растений
    fun getAllPlants(): List<Plant> {
        val db: SQLiteDatabase = databaseHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM ${PlantDatabaseHelper.TABLE_PLANTS}", null)
        val plants = mutableListOf<Plant>()
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(PlantDatabaseHelper.COLUMN_ID))
                val name = getString(getColumnIndexOrThrow(PlantDatabaseHelper.COLUMN_NAME))
                val description = getString(getColumnIndexOrThrow(PlantDatabaseHelper.COLUMN_DESCRIPTION))
                plants.add(Plant(id, name, description))
            }
        }
        cursor.close()
        return plants
    }
}
