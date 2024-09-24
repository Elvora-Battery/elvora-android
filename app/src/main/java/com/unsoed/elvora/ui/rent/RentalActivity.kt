package com.unsoed.elvora.ui.rent

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unsoed.elvora.R
import com.unsoed.elvora.data.response.getSubs.ActiveSubscription
import com.unsoed.elvora.databinding.ActivityRentalBinding
import com.unsoed.elvora.helper.formatDate

class RentalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRentalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRentalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dataRental = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(LIST_RENTAL, ActiveSubscription::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent?.getParcelableExtra(LIST_RENTAL)
        }

        dataRental?.let {
            val purchaseDate = formatDate(it.createdAt!!)
            val createdDate = formatDate(it.createdAt!!)
            binding.tvCvDatePurchase.text = "Purchase date $purchaseDate"
            binding.tvCvIdBattery.text = "EV${it.id}"
            binding.tvCvEndDate.text = "Purchase date $createdDate"
            binding.tvCvNameMotor.text = it.batteryName

            binding.cvRental.setOnClickListener {
                val intent = Intent(this@RentalActivity, RentalInformationActivity::class.java)
                intent.putExtra(RentalInformationActivity.EXTRA_DATA, dataRental)
                startActivity(intent)
            }
        }
    }

    companion object {
        const val LIST_RENTAL = "list_rental"
    }
}