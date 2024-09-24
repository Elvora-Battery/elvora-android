package com.unsoed.elvora.ui.rent

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unsoed.elvora.R
import com.unsoed.elvora.data.response.subs.AllSubsriptionsItem
import com.unsoed.elvora.databinding.ActivityRentalBinding

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
            intent.getParcelableExtra(LIST_RENTAL, AllSubsriptionsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent?.getParcelableExtra(LIST_RENTAL)
        }

        dataRental?.let {
            binding.tvCvDatePurchase.text = it.createdAt.toString()
            binding.tvCvIdBattery.text = "EV${it.id}"
            binding.tvCvEndDate.text = it.expirationDate.toString()
            binding.tvCvNameMotor.text = "Default Motorcycle EV${it.id}"

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