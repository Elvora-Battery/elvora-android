package com.unsoed.elvora.ui.subs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unsoed.elvora.R
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.data.response.getSubs.AllSubsriptionsItem
import com.unsoed.elvora.databinding.ActivityDetailTransactionBinding
import com.unsoed.elvora.helper.SubsModelFactory
import com.unsoed.elvora.helper.formatDate
import com.unsoed.elvora.helper.formatNumber
import com.unsoed.elvora.ui.rent.ActivateActivity

class DetailTransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTransactionBinding
    private val subsViewModel: SubsViewModel by viewModels {
        SubsModelFactory.getInstance(this)
    }
    private var tokenBattery = ""
    private var tokenBatteryExpired = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val detailTransaction = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DATA, AllSubsriptionsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DATA)
        }
        initView(detailTransaction?.id)

        if(detailTransaction != null) {
            binding.btnCopyText.setOnClickListener {
                val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", tokenBattery)
                clipboard.setPrimaryClip(clip)

                Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
            }

            val price = formatNumber(detailTransaction.rentType?.price!!)
            binding.apply {
                tvDtTypeBattery.text = if(detailTransaction.rentTypeId == 1) "72V 20Ah Battery" else "72V 40Ah Battery"
                tvDtId.text = "ID Battery : EV${detailTransaction.rentTypeId}"
                tvDetailStatus.text = detailTransaction.status
                tvDetailIdBattery.text = "EV${detailTransaction.id}"
                tvDetailOrderDate.text = formatDate(detailTransaction.createdAt!!)
                tvDetailPaymentMethod.text = detailTransaction.payment
                tvDtTotal.text = "Rp${price}"
            }
        } else {
            val batteryType = intent.getStringExtra(BATTERY_TYPE)
            val idBatteryType = intent.getStringExtra(ID_BATTERY_TYPE)
            val statusTransaction = intent.getStringExtra(STATUS_TRANSACTION)
            val idTransaction = intent.getStringExtra(ID_TRANSACTION)
            val dateTransaction = intent.getStringExtra(DATE_TRANSACTION)
            val paymentTransaction = intent.getStringExtra(PAYMENT_TRANSACTION)
            val totalPayment = intent.getStringExtra(TOTAL_PRICE)
            val token = intent.getStringExtra(TOKEN)

            binding.btnCopyText.setOnClickListener {
                val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", token)
                clipboard.setPrimaryClip(clip)

                Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
            }

            binding.apply {
                tvDtTypeBattery.text = batteryType
                tvDtId.text = "ID Battery : EV${idBatteryType}"
                tvDetailStatus.text = statusTransaction
                tvDetailIdBattery.text = idTransaction
                tvDetailOrderDate.text = dateTransaction
                tvDetailPaymentMethod.text = paymentTransaction
                tvDtTotal.text = "Rp$totalPayment.000"
                tvDtToken.text = token
                binding.tvDtExpired.text = tokenBatteryExpired
            }
        }

        binding.btnActivateToken.setOnClickListener {
            val intent = Intent(this@DetailTransactionActivity, ActivateActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initView(id: Int?) {
        if (id != null) {
            subsViewModel.getTransactionById(id).observe(this) {
                it?.let { response ->
                    when (response) {
                        is ApiResult.Loading -> {}

                        is ApiResult.Success -> {
                            tokenBattery = response.data.transaction?.token?.token.toString()
                            tokenBatteryExpired = formatDate(response.data.transaction?.expirationDate.toString())
                            binding.tvDtToken.text = response.data.transaction?.token?.token.toString()
                            binding.tvDtExpired.text = formatDate(response.data.transaction?.expirationDate.toString())
                        }

                        is ApiResult.Error -> {
                            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                            Log.d(TAG, response.message)
                        }

                        ApiResult.Empty -> {}
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "DetailTransactionActivi"
        const val BATTERY_TYPE = "battery_type"
        const val ID_BATTERY_TYPE = "id_battery_type"
        const val STATUS_TRANSACTION = "status_transaction"
        const val ID_TRANSACTION = "id_battery_type"
        const val DATE_TRANSACTION = "date_transaction"
        const val PAYMENT_TRANSACTION = "payment_transaction"
        const val TOTAL_PRICE = "total_price"
        const val TOKEN = "token"

        const val EXTRA_DATA = "extra_data"
    }
}