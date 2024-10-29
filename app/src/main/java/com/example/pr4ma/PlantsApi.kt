package com.example.pr4ma

import retrofit2.http.GET
import retrofit2.http.Query

interface PlantsApi {
    @GET("plants")
    suspend fun getPlants(@Query("name") plantName: String): List<Plant>
}
