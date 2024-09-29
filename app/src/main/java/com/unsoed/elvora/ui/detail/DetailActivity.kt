package com.unsoed.elvora.ui.detail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.databinding.ActivityDetailBinding
import com.unsoed.elvora.helper.SubsModelFactory
import com.unsoed.elvora.helper.formatDate
import com.unsoed.elvora.helper.formatNumber
import com.unsoed.elvora.ui.subs.SubsViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val subsViewModel: SubsViewModel by viewModels {
        SubsModelFactory.getInstance(this)
    }
    private var tokenBattery = ""
    private var tokenVerify = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        subsViewModel.getUserSession().observe(this) {
            it?.let { data ->
                binding.tvDetailName.text = data.fullName
            }
        }

        subsViewModel.getUserShipping().observe(this) {
            it?.let { data ->
                binding.tvDetailAddress.text = "${data.street}, ${data.village}, ${data.address}"
            }
        }

        val id = intent.getIntExtra(EXTRA_ID, 0)
        subsViewModel.getTransactionById(id).observe(this) {
            it?.let { response ->
                when (response) {
                    is ApiResult.Loading -> {
                        binding.ltLoading.visibility = View.VISIBLE
                    }

                    ApiResult.Empty -> {

                    }

                    is ApiResult.Error -> {
                        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                        binding.ltLoading.visibility = View.GONE
                    }

                    is ApiResult.Success -> {
                        binding.ltLoading.visibility = View.GONE
                        val data = response.data.transaction
                        val rent = response.data.rentType
                        tokenVerify = response.data.tokenVerify!!
                        binding.btnActivateDetail.isEnabled = !tokenVerify

                        data?.let { transaction ->
                            tokenBattery = transaction.token?.token.toString()
                            binding.apply {
                                tvIdTransaction.text = "Id Transaction: EV${transaction.id}"
                                tvDetailBatteryName.text = transaction.batteryName
                                cvOrder.tvDetailTitlePrice.text = "${transaction.batteryName} Fee"
                                tvDetailStatus.text = transaction.status
                                tvDetailIdBattery.text = "EV${transaction.rentTypeId}"
                                tvDetailOrderDate.text = formatDate(transaction.createdAt.toString())
                                tvDetailPaymentMethod.text = transaction.payment
                                tvDtToken.text = transaction.token?.token
                                tvDtExpired.text = formatDate(transaction.expirationDate.toString())
                            }
                        }

                        rent?.let { battery ->
                            val price = formatNumber(battery.price!!)
                            val totalPrice = battery.price + 1000 + 15000
                            binding.apply {
                                cvOrder.tvDetailHandlingPrice.text = "Rp1.000"
                                cvOrder.tvDetailDeliveryPrice.text = "Rp15.000"
                                cvOrder.tvDetailBatteryPrice.text = "Rp${price}"
                                cvOrder.tvTotalPrice.text = "Rp${formatNumber(totalPrice)}"
                            }
                        }
                    }
                }
            }
        }

        binding.btnActivateDetail.setOnClickListener {
            val bottomSheetDialog = ActivateDialogFragment()
            bottomSheetDialog.show(supportFragmentManager, ActivateDialogFragment.TAG)
        }

        binding.btnCopyText.setOnClickListener {
            val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", tokenBattery)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}