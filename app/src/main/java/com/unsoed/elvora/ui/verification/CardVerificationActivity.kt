package com.unsoed.elvora.ui.verification

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unsoed.elvora.R
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.databinding.ActivityCardVerificationBinding
import com.unsoed.elvora.helper.RentModelFactory
import com.unsoed.elvora.helper.uriToFile
import com.unsoed.elvora.ui.rent.RentViewModel
import com.unsoed.elvora.ui.verification.CameraActivity.Companion.CAMERAX_RESULT
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class CardVerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCardVerificationBinding
    private val rentViewModel: RentViewModel by viewModels {
        RentModelFactory.getInstance(this)
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivCardId.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCardVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.btnTakePhotoCard.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            launcherIntentCameraX.launch(intent)
        }

        binding.btnConfirmCard.setOnClickListener {
            uploadImage()
        }
    }

    private fun uploadKtp(multipartBody: MultipartBody.Part) {
        rentViewModel.uploadKtp(multipartBody).observe(this) {
            it?.let { response ->
                when (response) {
                    is ApiResult.Loading -> {
                        binding.ltLoading.visibility = View.VISIBLE
                        binding.btnConfirmCard.visibility = View.INVISIBLE
                    }

                    is ApiResult.Success -> {
                       val nik = response.data.nIK
                       val name = response.data.nama
                       val date = response.data.tanggalLahir

                        val intent = Intent(this@CardVerificationActivity, IdentifyVerificationActivity::class.java)
                        intent.putExtra(IdentifyVerificationActivity.KTP_NAME, name)
                        intent.putExtra(IdentifyVerificationActivity.KTP_NUMBER, nik)
                        intent.putExtra(IdentifyVerificationActivity.KTP_DATE, date)
                        startActivity(intent)
                    }

                    is ApiResult.Error -> {
                        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                        Log.d(TAG, response.message)
                        binding.ltLoading.visibility = View.GONE
                        binding.btnConfirmCard.visibility = View.VISIBLE
                    }

                    ApiResult.Empty -> {

                    }

                    else -> {}
                }
            }
        }

    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this)
            Log.d("Image File", "showImage: ${imageFile.path}")
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData(
                "file",
                imageFile.name,
                requestImageFile
            )

            uploadKtp(multipartBody)
        } ?: showToast("Image should not be empty")
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    companion object {
        private const val TAG = "CardVerificationActivity"
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}