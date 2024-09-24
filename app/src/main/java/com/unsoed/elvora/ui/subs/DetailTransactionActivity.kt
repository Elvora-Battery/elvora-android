package com.unsoed.elvora.ui.subs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unsoed.elvora.R
import com.unsoed.elvora.databinding.ActivityDetailTransactionBinding
import com.unsoed.elvora.ui.rent.ActivateActivity

class DetailTransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTransactionBinding

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
            tvDtId.text = "ID Battery : idBatteryType"
            tvDetailStatus.text = statusTransaction
            tvDetailIdBattery.text = idTransaction
            tvDetailOrderDate.text = dateTransaction
            tvDetailPaymentMethod.text = paymentTransaction
            tvDtTotal.text = "Rp$totalPayment.000"
            tvDtToken.text = token
        }

        binding.btnActivateToken.setOnClickListener {
            val intent = Intent(this@DetailTransactionActivity, ActivateActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        const val BATTERY_TYPE = "battery_type"
        const val ID_BATTERY_TYPE = "id_battery_type"
        const val STATUS_TRANSACTION = "status_transaction"
        const val ID_TRANSACTION = "id_battery_type"
        const val DATE_TRANSACTION = "date_transaction"
        const val PAYMENT_TRANSACTION = "payment_transaction"
        const val TOTAL_PRICE = "total_price"
        const val TOKEN = "token"
    }
}