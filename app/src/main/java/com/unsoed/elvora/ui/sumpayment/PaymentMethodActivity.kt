package com.unsoed.elvora.ui.sumpayment

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unsoed.elvora.R
import com.unsoed.elvora.data.Payment
import com.unsoed.elvora.databinding.ActivityPaymentMethodBinding
import java.text.DecimalFormat

class PaymentMethodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentMethodBinding
    private var clickButtonVa: Boolean = false
    private var clickButtonVw: Boolean = false
    private var clickButtonRetail: Boolean = false

    var selectedRadio = false

    private var idTransaction = 0
    private var totalSum: String = ""
    private var batteryType: String = ""
    private var batteryId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPaymentMethodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sumPayment = intent.getDoubleExtra(PAYMENT_PRICE, 0.0)
        val idPayment = intent.getIntExtra(TRANSACTION_ID, 0)
        batteryType = intent.getStringExtra(BATTERY_TYPE) ?: ""
        batteryId = intent.getStringExtra(BATTERY_ID) ?: ""

        idTransaction = idPayment

        val decimalFormat = DecimalFormat("#,###")
        val formattedTotal = decimalFormat.format(sumPayment)
        totalSum = formattedTotal
        binding.tvPricePayment.text = "Rp$formattedTotal.000"

        setupAction()
        setupRadio()
    }

    private fun setupRadio() {
        binding.rgVirtualAccount.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId != -1) {
                binding.rgRetail.clearCheck()
                binding.rgVirtualWallet.clearCheck()
            }
        }

        binding.rgVirtualWallet.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId != -1) {
                binding.rgVirtualAccount.clearCheck()
                binding.rgRetail.clearCheck()
            }
        }

        binding.rgRetail.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId != -1) {
                binding.rgVirtualWallet.clearCheck()
                binding.rgVirtualAccount.clearCheck()
            }
        }
    }

    private fun checkRadioVaChecked(checkId: Int) {
        val dataPayment = when (checkId) {
            R.id.rb_payment_bca -> Payment(
                name = "Bank BCA",
                image = R.drawable.icon_bank_bca,
                desc = "Complete payment from Bank BCA to the virtual account number below.",
                number = "33428327427617829"
            )

            R.id.rb_payment_bni -> Payment(
                name = "Bank BNI",
                image = R.drawable.icon_bank_bni,
                desc = "Complete payment from Bank BNI to the virtual account number below.",
                number = "33443327427427832"
            )

            R.id.rb_payment_mandiri -> Payment(
                name = "Bank Mandiri",
                image = R.drawable.icon_bank_mandiri,
                desc = "Complete payment from Bank Mandiri to the virtual account number below.",
                number = "31456326527197833"
            )

            R.id.rb_payment_permata -> Payment(
                name = "Bank Permata",
                image = R.drawable.icon_bank_permata,
                desc = "Complete payment from Bank Permata to the virtual account number below.",
                number = "33428327427617829"
            )

            R.id.rb_payment_briva -> Payment(
                name = "Bank BRI",
                image = R.drawable.icon_bank_bri,
                desc = "Complete payment from Bank BRI to the virtual account number below.",
                number = "31428403227117822"
            )

            else -> null
        }
        val modalBottomSheet = PaymentDialogFragment()
        modalBottomSheet.show(supportFragmentManager, PaymentDialogFragment.TAG)
        modalBottomSheet.arguments = Bundle().apply {
            putParcelable(PaymentDialogFragment.EXTRA_DATA, dataPayment)
            putInt(PaymentDialogFragment.ID_TRANSACTION, idTransaction)
            putString(PaymentDialogFragment.ID_BATTERY, batteryId)
            putString(PaymentDialogFragment.TYPE_BATTERY, batteryType)
            putString(PaymentDialogFragment.TOTAL_PRICE, totalSum)
        }
    }

    private fun setupAction() {
        binding.btnPayment.setOnClickListener {
            val checkedRadioButtonVa = binding.rgVirtualAccount.checkedRadioButtonId
            val checkedRadioButtonVw = binding.rgVirtualWallet.checkedRadioButtonId
            val checkedRadioButtonRetail = binding.rgRetail.checkedRadioButtonId

            if (checkedRadioButtonVa != -1) {
                checkRadioVaChecked(checkedRadioButtonVa)
            }
            if (checkedRadioButtonVw != -1) {
                checkRadioVwChecked(checkedRadioButtonVw)
            }
            if (checkedRadioButtonRetail != -1) {
                checkRadioRetailChecked(checkedRadioButtonRetail)
            }
        }

        binding.btnArrowBack.setOnClickListener {
            OnBackPressedDispatcher().onBackPressed()
            finish()
        }

        binding.btnVirtualAccount.setOnClickListener {
            if (clickButtonVa) {
                binding.rgVirtualAccount.visibility = View.GONE
                binding.btnElectronicWallet.setCompoundDrawablesWithIntrinsicBounds(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.icon_virtual_account
                    ), null, AppCompatResources.getDrawable(this, R.drawable.icon_dropdown), null
                )
            } else {
                binding.rgVirtualAccount.visibility = View.VISIBLE
                binding.btnRetail.setCompoundDrawablesWithIntrinsicBounds(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.icon_virtual_account
                    ), null, AppCompatResources.getDrawable(this, R.drawable.icon_arrow_up), null
                )
            }
            clickButtonVa = !clickButtonVa
        }

        binding.btnElectronicWallet.setOnClickListener {
            if (clickButtonVw) {
                binding.rgVirtualWallet.visibility = View.GONE
                binding.btnElectronicWallet.setCompoundDrawablesWithIntrinsicBounds(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.icon_wallet
                    ), null, AppCompatResources.getDrawable(this, R.drawable.icon_dropdown), null
                )
            } else {
                binding.btnRetail.setCompoundDrawablesWithIntrinsicBounds(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.icon_wallet
                    ), null, AppCompatResources.getDrawable(this, R.drawable.icon_arrow_up), null
                )
                binding.rgVirtualWallet.visibility = View.VISIBLE
            }
            clickButtonVw = !clickButtonVw
        }

        binding.btnRetail.setOnClickListener {
            if (clickButtonRetail) {
                binding.rgRetail.visibility = View.GONE
                binding.btnRetail.setCompoundDrawablesWithIntrinsicBounds(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.icon_retail
                    ), null, AppCompatResources.getDrawable(this, R.drawable.icon_dropdown), null
                )
            } else {
                binding.btnRetail.setCompoundDrawablesWithIntrinsicBounds(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.icon_retail
                    ), null, AppCompatResources.getDrawable(this, R.drawable.icon_arrow_up), null
                )
                binding.rgRetail.visibility = View.VISIBLE
            }
            clickButtonRetail = !clickButtonRetail
        }
    }

    private fun checkRadioRetailChecked(checkedRadioButtonRetail: Int) {
        val dataPayment = when (checkedRadioButtonRetail) {
            R.id.rb_alfamart -> Payment(
                name = "Alfamart",
                image = R.drawable.logo_alfamart,
                desc = "Complete payment from Alfamart Retail to the virtual account number below.",
                number = "08428332427617829"
            )
            else -> null
        }
        val modalBottomSheet = PaymentDialogFragment()
        modalBottomSheet.show(supportFragmentManager, PaymentDialogFragment.TAG)
        modalBottomSheet.arguments = Bundle().apply {
            putParcelable(PaymentDialogFragment.EXTRA_DATA, dataPayment)
            putInt(PaymentDialogFragment.ID_TRANSACTION, idTransaction)
        }
    }

    private fun checkRadioVwChecked(checkedRadioButtonVw: Int) {
        val dataPayment = when (checkedRadioButtonVw) {
            R.id.rb_payment_dana -> Payment(
                name = "Dana",
                image = R.drawable.dana,
                desc = "Complete payment from Dana to the virtual account number below.",
                number = "085875814528"
            )

            R.id.rb_payment_gopay -> Payment(
                name = "Gopay",
                image = R.drawable.gopay,
                desc = "Complete payment from Gopay to the virtual account number below.",
                number = "085875814528"
            )

            R.id.rb_payment_ovo -> Payment(
                name = "OVO",
                image = R.drawable.logo_ovo,
                desc = "Complete payment from OVO to the virtual account number below.",
                number = "085875814528"
            )

            else -> null
        }
        val modalBottomSheet = PaymentDialogFragment()
        modalBottomSheet.show(supportFragmentManager, PaymentDialogFragment.TAG)
        modalBottomSheet.arguments = Bundle().apply {
            putParcelable(PaymentDialogFragment.EXTRA_DATA, dataPayment)
            putInt(PaymentDialogFragment.ID_TRANSACTION, idTransaction)
        }
    }

    companion object {
        const val PAYMENT_PRICE = "payment_price"
        const val TRANSACTION_ID = "transaction_id"
        const val BATTERY_ID = "battery_id"
        const val BATTERY_TYPE = "battery_type"
    }
}