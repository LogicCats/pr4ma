package com.example.pr4ma

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class AddPlantFragment : Fragment(R.layout.fragment_add_plant) {

    @Inject
    lateinit var plantDao: PlantDao  // Используем Hilt для инъекции зависимости

    private lateinit var plantNameEditText: EditText
    private lateinit var plantDescriptionEditText: EditText
    private lateinit var addPlantButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        plantNameEditText = view.findViewById(R.id.et_plant_name)
        plantDescriptionEditText = view.findViewById(R.id.et_plant_description)
        addPlantButton = view.findViewById(R.id.btn_add_plant)

        addPlantButton.setOnClickListener {
            val name = plantNameEditText.text.toString()
            val description = plantDescriptionEditText.text.toString()

            if (name.isNotEmpty() && description.isNotEmpty()) {
                addPlantToDatabase(Plant(name = name, description = description))
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addPlantToDatabase(plant: Plant) {
        lifecycleScope.launch {
            plantDao.insertPlant(plant)
            Toast.makeText(requireContext(), "Plant added successfully", Toast.LENGTH_SHORT).show()


            plantNameEditText.text.clear()
            plantDescriptionEditText.text.clear()
        }
    }
}
