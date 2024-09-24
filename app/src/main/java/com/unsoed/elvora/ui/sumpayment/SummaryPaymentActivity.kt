package com.unsoed.elvora.ui.sumpayment

import android.content.Intent
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
import com.unsoed.elvora.databinding.ActivitySummaryPaymentBinding
import com.unsoed.elvora.helper.RentModelFactory
import com.unsoed.elvora.ui.rent.RentViewModel
import java.text.DecimalFormat

class SummaryPaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummaryPaymentBinding
    private val rentViewModel: RentViewModel by viewModels {
        RentModelFactory.getInstance(this)
    }
    private var shippingInformation: UserShippingModel? = null
    private var firstLaunch: Boolean= true
    private var paymentTotal: Double? = null
    private var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySummaryPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initView()
        setupAction()
    }

    override fun onResume() {
        super.onResume()
        if(!firstLaunch) {
            rentViewModel.getUserShipping().observe(this) {
                it?.let {
                    setupCardContactPayment(it)
                }
            }
        }
    }

    private fun setupAction() {
        binding.btnArrowBack.setOnClickListener {
            OnBackPressedDispatcher().onBackPressed()
            finish()
        }

        binding.cvContactPayment.btnEditContact.setOnClickListener {
            firstLaunch = false
            val intent = Intent(this, ShippingDetailActivity::class.java)
            intent.putExtra(ShippingDetailActivity.EXTRA_DATA, shippingInformation)
            startActivity(intent)
        }

        binding.btnPayNow.setOnClickListener {
            if(shippingInformation == null) {
                Toast.makeText(this, "Fill in all your shipping information", Toast.LENGTH_SHORT).show()
            } else {
                id?.let { id ->
                    rentViewModel.newTransaction(idRent = id).observe(this) {
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
                                    Toast.makeText(this, data.data.status, Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this@SummaryPaymentActivity, PaymentMethodActivity::class.java)
                                    intent.putExtra(PaymentMethodActivity.PAYMENT_PRICE, paymentTotal)
                                    intent.putExtra(PaymentMethodActivity.TRANSACTION_ID, data.data.id)
                                    intent.putExtra(PaymentMethodActivity.BATTERY_TYPE, data.data.batteryName)
                                    intent.putExtra(PaymentMethodActivity.BATTERY_ID, data.data.rentTypeId)
                                    startActivity(intent)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupCardContactPayment(data: UserShippingModel) {
        if(data.name.isEmpty()) {
            binding.cvContactPayment.apply {
                tvAiValue1.text = "Enter your data information"
                tvAiValue2.text = "Enter your data information"
                tvAiValue3.text = "Enter your data information"
            }
        } else {
            shippingInformation = UserShippingModel(
                name = data.name,
                telephoneNumber = data.telephoneNumber,
                street = data.street,
                village = data.village,
                address = data.address
            )

            binding.cvContactPayment.apply {
                tvAiValue1.text = data.name
                tvAiValue2.text = data.telephoneNumber
                tvAiValue3.text = "${data.street}, ${data.village}, ${data.address}"
            }
        }
    }

    private fun initView() {
        val typeBattery = intent.getStringExtra(BATTERY_TYPE)
        val priceDoubleBattery = intent.getDoubleExtra(BATTERY_PRICE_INT, 0.0)
        val descBattery = intent.getStringExtra(BATTERY_DESC)
        val priceBattery = intent.getStringExtra(BATTERY_PRICE)
        val idBattery = intent.getStringExtra(BATTERY_RENT_ID)

        idBattery?.let {
            id = it
        }

        binding.tvBatteryType.text = typeBattery
        binding.tvBatteryPrice.text = priceBattery
        binding.tvBatteryDesc.text = descBattery

        rentViewModel.getUserShipping().observe(this) {
            it?.let {
                setupCardContactPayment(it)
            }
        }

        val totalPayment = 15.000 + 1.000 + priceDoubleBattery
        val decimalFormat = DecimalFormat("#,###")
        val formattedTotal = decimalFormat.format(totalPayment)
        paymentTotal = totalPayment


        binding.cvContactPayment.apply {
            tvAiTitle1.text = "Name"
            tvAiTitle2.text = "Telephone Number"
            tvAiTitle3.text = "Address"
            tvCardTitle.text = "Shipping Details"
        }

        binding.cvPaymentDetail.apply {
            tvTotalPrice.text = "Rp$formattedTotal.000"
            tvDetailTitlePrice.text = "$typeBattery Fee"

            tvDetailDeliveryPrice.text = "Rp15.000"
            tvDetailHandlingPrice.text = "Rp1.000"
            tvDetailBatteryPrice.text = "$priceBattery"
        }
    }

    companion object {
        const val BATTERY_RENT_ID = "battery_rent_id"
        const val BATTERY_TYPE = "battery_type"
        const val BATTERY_PRICE_INT = "battery_price_int"
        const val BATTERY_DESC = "battery_desc"
        const val BATTERY_PRICE = "battery_price"
        const val FULL_NAME = "full_name"
    }
}