package com.unsoed.elvora.ui.rent

import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unsoed.elvora.R
import com.unsoed.elvora.data.response.subs.AllSubsriptionsItem
import com.unsoed.elvora.databinding.ActivityRentalInformationBinding
import com.unsoed.elvora.ui.subs.ChangeNameDialogFragment

class RentalInformationActivity : AppCompatActivity(), ChangeNameDialogFragment.OnNameChangeListener {

    private lateinit var binding: ActivityRentalInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRentalInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val rentalBattery = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DATA, AllSubsriptionsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DATA)
        }

        rentalBattery?.let {
            binding.apply {
                tvIdTransaction.text = "EV${it.id}"
                tvRentalCategory.text = if(it.rentTypeId == 1) "72V 20Ah Battery" else "72V 40Ah Battery"
                tvRentalStatus.text = it.status
                tvRentalBatteryName.text = it.batteryName
                tvRentalOrderDate.text = it.createdAt.toString()
                tvRentalEndsDate.text = it.expirationDate.toString()
            }
        }

        binding.btnArrowBack.setOnClickListener {
            OnBackPressedDispatcher().onBackPressed()
        }

        binding.tvChangeName.setOnClickListener {
            val modalBottomSheet  = ChangeNameDialogFragment()
            modalBottomSheet.show(supportFragmentManager, ChangeNameDialogFragment.TAG)
            modalBottomSheet.arguments = Bundle().apply {
                putInt(ChangeNameDialogFragment.BATTERY_ID, rentalBattery?.id ?: 0)
                putString(ChangeNameDialogFragment.BATTERY_NAME, rentalBattery?.batteryName)
            }
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    override fun onNameChanged(newName: String) {
        binding.tvRentalBatteryName.text = newName
    }
}