package com.example.pr4ma

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.pr4ma.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class ImageDownloadFragment : Fragment() {

    private lateinit var urlEditText: EditText
    private lateinit var downloadButton: Button
    private lateinit var imagePathTextView: TextView
    private lateinit var downloadedImageView: ImageView

    companion object {
        private const val TAG = "ImageDownloadFragment"
        private const val INTERNET_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_download, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        urlEditText = view.findViewById(R.id.et_image_url)
        downloadButton = view.findViewById(R.id.btn_download)
        imagePathTextView = view.findViewById(R.id.tv_image_path)
        downloadedImageView = view.findViewById(R.id.iv_downloaded_image)

        // Проверка разрешений
        checkInternetPermission()

        downloadButton.setOnClickListener {
            val imageUrl = urlEditText.text.toString()
            if (imageUrl.isNotEmpty()) {
                Log.d(TAG, "Attempting to download image from $imageUrl")
                downloadImage(imageUrl)
            } else {
                Toast.makeText(requireContext(), "Please enter a valid URL", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkInternetPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.INTERNET), INTERNET_PERMISSION_REQUEST_CODE)
        } else {
            Log.d(TAG, "Internet permission already granted.")
        }
    }

    private fun downloadImage(imageUrl: String) {
        lifecycleScope.launch {
            // Вызов корутины для загрузки изображения
            val bitmap = withContext(Dispatchers.IO) { fetchImage(imageUrl) }
            bitmap?.let {
                Log.d(TAG, "Image downloaded successfully.")
                // Вызов корутины для сохранения изображения на диск
                saveImageToDisk(it)
            } ?: Log.e(TAG, "Failed to download image.")
        }
    }

    private fun fetchImage(url: String): Bitmap? {
        return try {
            Log.d(TAG, "Fetching image from $url")
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(input)
            if (bitmap != null) {
                Log.d(TAG, "Image fetched successfully, width: ${bitmap.width}, height: ${bitmap.height}")
            } else {
                Log.e(TAG, "Failed to decode bitmap from input stream.")
            }
            bitmap
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching image: ${e.message}")
            null
        }
    }

    private fun saveImageToDisk(bitmap: Bitmap) {
        lifecycleScope.launch(Dispatchers.IO) {
            val file = File(requireContext().filesDir, "downloaded_image.png")
            try {
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                    withContext(Dispatchers.Main) {
                        imagePathTextView.text = "Image saved to: ${file.absolutePath}"
                        downloadedImageView.setImageBitmap(bitmap)
                        downloadedImageView.visibility = View.VISIBLE
                        Log.d(TAG, "Image saved to ${file.absolutePath}")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save image: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Failed to save image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
