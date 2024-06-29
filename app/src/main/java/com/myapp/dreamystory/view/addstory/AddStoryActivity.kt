package com.myapp.dreamystory.view.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.myapp.dreamystory.R
import com.myapp.dreamystory.databinding.ActivityAddStoryBinding
import com.myapp.dreamystory.view.ViewModelFactory
import com.myapp.dreamystory.view.main.MainActivity
import com.myapp.dreamystory.view.utils.getImageUri
import com.myapp.dreamystory.view.utils.reduceFileImage
import com.myapp.dreamystory.view.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryActivity : AppCompatActivity() {
    private val viewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var currentImageUri: Uri? = null
    private var storyLat: Double? = null
    private var storyLon: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.buttonAdd.setOnClickListener {
            if (binding.checkboxStoryLocation.isChecked) {
                getMyLastLocation()
            } else {
                uploadImage()
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                else -> { }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if     (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    storyLat = location.latitude
                    storyLon = location.longitude
                    uploadImage()
                } else {
                    showToast(getString(R.string.story_loc_failed))
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun uploadImage() {
        showLoading()
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo", imageFile.name, requestImageFile
            )

            val requestBody = binding.edAddDescription.text.toString()
            if (requestBody.isEmpty()) {
                showToast(getString(R.string.empty_desc))
            } else {
                val description = binding.edAddDescription.text.toString().toRequestBody("text/plain".toMediaType())

                viewModel.getNewStory(multipartBody, description, storyLat, storyLon)

                viewModel.getLoginToken().observe(this) {
                    AlertDialog.Builder(this).apply {
                        setTitle("Yoo-hoo!")
                        setMessage("Your story uploaded successfully..")
                        setPositiveButton("OK") { _, _ ->
                            val intentStory =
                                Intent(context, MainActivity::class.java)
                            intentStory.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intentStory)
                            finish()
                        }
                        create()
                        show()
                    }
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading() {
        viewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
    }
}