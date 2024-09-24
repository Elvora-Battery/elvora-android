package com.unsoed.elvora.ui.sumpayment

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unsoed.elvora.R
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.data.UserShippingModel
import com.unsoed.elvora.databinding.ActivityShippingDetailBinding
import com.unsoed.elvora.helper.RentModelFactory
import com.unsoed.elvora.ui.rent.RentViewModel

class ShippingDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShippingDetailBinding
    private var isEdit: Boolean = false
    private var shippingData: UserShippingModel? = null
    private val rentViewModel: RentViewModel by viewModels {
        RentModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityShippingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initView()
        setupAction()
    }

    private fun setupAction() {
        binding.btnArrowBack.setOnClickListener {
            OnBackPressedDispatcher().onBackPressed()
            finish()
        }

        binding.btnShipping.setOnClickListener {
            val name = binding.etSdInputName.editText?.text.toString()
            val number = binding.etSdInputNumber.editText?.text.toString()
            val street = binding.etSdInputStreet.editText?.text.toString()
            val village = binding.etSdInputVillage.editText?.text.toString()
            val address = binding.etSdInputAddress.editText?.text.toString()

            when {
                name.isEmpty() -> {
                    binding.etSdInputName.error = "Please fill out this field"
                }
                number.isEmpty() -> {
                    binding.etSdInputNumber.error = "Please fill out this field"
                }
                street.isEmpty() -> {
                    binding.etSdInputStreet.error = "Please fill out this field"
                }
                village.isEmpty() -> {
                    binding.etSdInputVillage.error = "Please fill out this field"
                }
                address.isEmpty() -> {
                    binding.etSdInputAddress.error = "Please fill out this field"
                }
                else -> {
                    if(isEdit) {
                        Toast.makeText(this, "Cannot edit the data form at the moment. Please wait for the next update.", Toast.LENGTH_SHORT).show()
                    } else {
                        rentViewModel.createShippingUser(
                            UserShippingModel(
                                name = name,
                                telephoneNumber = number,
                                street = street,
                                village = village,
                                address = address
                            )).observe(this) {
                            it?.let { data ->
                                when(data) {
                                    is ApiResult.Loading -> {

                                    }

                                    ApiResult.Empty -> {

                                    }

                                    is ApiResult.Error -> {
                                        Toast.makeText(this, data.message, Toast.LENGTH_SHORT).show()
                                    }

                                    is ApiResult.Success -> {
                                        Toast.makeText(this, "Success create new shipping information", Toast.LENGTH_SHORT).show()
                                        OnBackPressedDispatcher().onBackPressed()
                                        finish()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initView() {
        shippingData= if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DATA, UserShippingModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DATA)
        }

        if(shippingData != null) {
            isEdit = true
            binding.etSdInputName.editText?.setText(shippingData?.name)
            binding.etSdInputNumber.editText?.setText(shippingData?.telephoneNumber)
            binding.etSdInputStreet.editText?.setText(shippingData?.street)
            binding.etSdInputVillage.editText?.setText(shippingData?.village)
            binding.etSdInputAddress.editText?.setText(shippingData?.address)
            binding.btnShipping.text = "Edit"
        } else {
            binding.btnShipping.text = "Save"
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}