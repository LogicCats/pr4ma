package com.example.pr4ma

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlantDatabaseFragment : Fragment(R.layout.fragment_plant_database) {
    @Inject
    lateinit var plantDao: PlantDao  // Используем Hilt для инъекции зависимости

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val plantDataTextView: TextView = view.findViewById(R.id.tv_plant_data)

        lifecycleScope.launch {
            val plants = withContext(Dispatchers.IO) { plantDao.getAllPlants() }
            if (plants.isNotEmpty()) {
                val plantData = plants.joinToString(separator = "\n") { plant ->
                    "${plant.name}: ${plant.description}"
                }
                plantDataTextView.text = plantData
            } else {
                plantDataTextView.text = "No plants in the database"
            }
        }
    }
}
