package com.example.pr4ma

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePlantDatabaseHelper(@ApplicationContext context: Context): PlantDatabaseHelper {
        return PlantDatabaseHelper(context)  // Инициализация нашего SQLiteOpenHelper
    }

    @Provides
    @Singleton
    fun providePlantDao(databaseHelper: PlantDatabaseHelper): PlantDao {
        return PlantDao(databaseHelper)  // Передаем helper в PlantDao для работы с SQLite
    }
}
