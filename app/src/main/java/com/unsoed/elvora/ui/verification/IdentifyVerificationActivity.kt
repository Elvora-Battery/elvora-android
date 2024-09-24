package com.unsoed.elvora.ui.verification

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unsoed.elvora.R
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.databinding.ActivityIdentifyVerificationctivityBinding
import com.unsoed.elvora.helper.RentModelFactory
import com.unsoed.elvora.helper.formatDateString
import com.unsoed.elvora.ui.rent.RentViewModel

class IdentifyVerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIdentifyVerificationctivityBinding
    private val rentViewModel: RentViewModel by viewModels {
        RentModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityIdentifyVerificationctivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val ktpName = intent.getStringExtra(KTP_NAME)
        val ktpNumber = intent.getStringExtra(KTP_NUMBER)
        val ktpDate = intent.getStringExtra(KTP_DATE)

        val formattedDate= ktpDate?.replace("-", "/")


        binding.apply {
            etCvInputName.editText?.setText(ktpName)
            etCvInputNumber.editText?.setText(ktpNumber)
            etCvInputBirth.editText?.setText(formattedDate)
        }

        binding.btnConfirmCardData.setOnClickListener {
            val name = binding.etCvInputName.editText?.text.toString()
            val number = binding.etCvInputNumber.editText?.text.toString()
            val date = binding.etCvInputBirth.editText?.text.toString()

            val dateFormatter = formatDateString(date)
            when {
                name.isEmpty() -> {
                    binding.etCvInputName.error = "Please fill out this field"
                }

                number.isEmpty() -> {
                    binding.etCvInputNumber.error = "Please fill out this field"
                }

                date.isEmpty() -> {
                    binding.etCvInputBirth.error = "Please fill out this field"
                }

                else -> {
                    rentViewModel.verificationKtp(nik = number, name = name, date = dateFormatter!!)
                        .observe(this) {
                            it?.let { data ->
                                when (data) {
                                    is ApiResult.Loading -> {

                                    }

                                    ApiResult.Empty -> {

                                    }

                                    is ApiResult.Error -> {
                                        Toast.makeText(this, data.message, Toast.LENGTH_SHORT)
                                            .show()
                                    }

                                    is ApiResult.Success -> {
                                        val intent =
                                            Intent(this, CompleteVerificationActivity::class.java)
                                        intent.putExtra(
                                            CompleteVerificationActivity.STATE,
                                            "Verify"
                                        )
                                        startActivity(intent)
                                        Toast.makeText(this, data.data, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                }
            }
        }
    }

    companion object {
        const val KTP_NAME = "ktp_name"
        const val KTP_NUMBER = "ktp_number"
        const val KTP_DATE = "ktp_date"
    }
}