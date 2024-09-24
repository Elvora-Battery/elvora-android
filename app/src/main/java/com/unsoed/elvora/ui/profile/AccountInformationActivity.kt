package com.unsoed.elvora.ui.profile

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unsoed.elvora.R
import com.unsoed.elvora.data.UserModel
import com.unsoed.elvora.data.UserShippingModel
import com.unsoed.elvora.data.UserVerify
import com.unsoed.elvora.databinding.ActivityAccountInformationBinding

class AccountInformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountInformationBinding
    private var userModel: UserModel? = null
    private var userShippingModel: UserShippingModel? = null
    private var userVerifyModel: UserVerify? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAccountInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userModel = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DATA_CONTACT, UserModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DATA_CONTACT)
        }

        userShippingModel = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DATA_PERSONAL, UserShippingModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DATA_PERSONAL)
        }

        userVerifyModel = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DATA_VERIFY, UserVerify::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DATA_VERIFY)
        }

        binding.btnArrowBack.setOnClickListener {
            OnBackPressedDispatcher().onBackPressed()
            finish()
        }

        setupCardContact()
        setupCardPersonal()
    }

    private fun setupCardPersonal() {
        binding.cvPersonalInformation.apply {
            tvCardTitle.text = "Personal Information"
            tvAiTitle1.text = "NIK"
            tvAiTitle2.text = "Date of Birth"
            tvAiTitle3.text = "Address"
            tvAiValue3.text = "${userShippingModel?.street}, ${userShippingModel?.village}, ${userShippingModel?.address}"
            tvAiValue2.text = userVerifyModel?.date ?: "Empty"
            tvAiValue1.text = userVerifyModel?.nik ?: "Empty"
        }

        binding.cvPersonalInformation.btnEditContact.setOnClickListener {
            Toast.makeText(this, "The feature is still under development, please try again later.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupCardContact() {
        binding.cvContactInformation.apply {
            tvCardTitle.text = "Contact Information"
            tvAiTitle1.text = "Full Name"
            tvAiTitle2.text = "Email"
            tvAiTitle3.text = "Telephone Number"

            tvAiValue1.text = userModel?.fullName
            tvAiValue2.text = userModel?.email
            tvAiValue3.text = userShippingModel?.telephoneNumber
        }

        binding.cvContactInformation.btnEditContact.setOnClickListener {
            Toast.makeText(this, "The feature is still under development, please try again later.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val EXTRA_DATA_CONTACT = "extra_data_contact"
        const val EXTRA_DATA_PERSONAL = "extra_data_personal"
        const val EXTRA_DATA_VERIFY = "extra_data_verify"
    }
}