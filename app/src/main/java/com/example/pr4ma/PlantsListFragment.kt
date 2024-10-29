package com.example.pr4ma

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlantsListFragment : Fragment(R.layout.fragment_plants_list) {
    @Inject
    lateinit var plantDao: PlantDao  // Используем Hilt для инъекции зависимости

    private lateinit var plantNameEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var resultTextView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        plantNameEditText = view.findViewById(R.id.et_plant_name)
        searchButton = view.findViewById(R.id.btn_search)
        resultTextView = view.findViewById(R.id.tv_plant_data)

        searchButton.setOnClickListener {
            val plantName = plantNameEditText.text.toString()
            if (plantName.isNotEmpty()) {
                searchPlantByName(plantName)
            } else {
                Toast.makeText(requireContext(), "Please enter a plant name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchPlantByName(plantName: String) {
        lifecycleScope.launch {
            val plants = withContext(Dispatchers.IO) { plantDao.getAllPlants() }
            val foundPlants = plants.filter { it.name.contains(plantName, ignoreCase = true) }
            if (foundPlants.isNotEmpty()) {
                val plantData = foundPlants.joinToString(separator = "\n") { plant ->
                    "${plant.name}: ${plant.description}"
                }
                resultTextView.text = plantData
            } else {
                resultTextView.text = "No plants found with the name '$plantName'"
            }
        }
    }
}
