//package com.example.pr4ma
//
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import android.content.Context
//
//@Database(entities = [Plant::class], version = 1, exportSchema = false)
//abstract class PlantsDatabase : RoomDatabase() {
//    abstract fun plantDao(): PlantDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: PlantsDatabase? = null
//
//        fun getInstance(context: Context): PlantsDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    PlantsDatabase::class.java,
//                    "plants_database"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}
