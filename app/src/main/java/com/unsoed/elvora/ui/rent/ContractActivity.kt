package com.unsoed.elvora.ui.rent

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.unsoed.elvora.R
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.databinding.ActivityContractBinding
import com.unsoed.elvora.helper.RentModelFactory
import com.unsoed.elvora.ui.sumpayment.PaymentMethodActivity

class ContractActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContractBinding
    private val rentViewModel: RentViewModel by viewModels {
        RentModelFactory.getInstance(this)
    }
    private var name = ""
    private var idBattery = ""
    private var paymentTotal: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityContractBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rentViewModel.getUserSession().observe(this) {
            it?.let {
                name = it.fullName
            }
        }

        paymentTotal = intent.getDoubleExtra(PAYMENT_PRICE, 0.0)
        val id = intent.getStringExtra(BATTERY_ID)
        id?.let {
            idBattery = id
        }

        setupAction()

        val htmlText1 = "<ol>" +
                "<li>The First Party agrees to rent to the Second Party, and the Second Party agrees to rent from the First Party, a battery [Type/Model of Battery] (hereinafter referred to as the \"Battery\").</li>" +
                "<li>The Battery is rented in good and usable condition by the Second Party.</li>" +
                "</ol>"

        binding.tvArticle1.text = Html.fromHtml(htmlText1, Html.FROM_HTML_MODE_LEGACY)

        val htmlText2 = "<ol>" +
                "<li>The rental period for the Battery is 3 years starting from 28 August 2024 and ending on  28 August 2027.</li>" +
                "<li>The The Second Party must return the Battery to the First Party on or before the end of the rental period.</li>" +
                "</ol>"

        binding.tvArticle2.text = Html.fromHtml(htmlText2, Html.FROM_HTML_MODE_LEGACY)

        val htmlText3 = "<ol>" +
                "<li>For the first-time rental, the Second Party is required to pay a rental fee of Rp266.000.</li>" +
                "<li>The rental fee for the Battery is Rp250.000 for the agreed rental period.</li>" +
                "<li>Payment must be made in advance before the Battery is handed over to the Second Party.</li>" +
                "<li>The Second Party will be charged a late fee of Rp1.000 per day if the Battery is not returned on time.\n</li>" +
                "</ol>"

        binding.tvArticle3.text = Html.fromHtml(htmlText3, Html.FROM_HTML_MODE_LEGACY)

        val htmlText4 = "<ol>" +
                "<li>The Second Party is responsible for maintaining and taking good care of the Battery during the rental period.</li>" +
                "<li>The Second Party is not allowed to modify or damage the Battery.</li>" +
                "<li>If the Battery is damaged or lost during the rental period, the Second Party must compensate for the loss according to the value of the Battery determined by the First Party.</li>" +
                "</ol>"

        binding.tvArticle4.text = Html.fromHtml(htmlText4, Html.FROM_HTML_MODE_LEGACY)

        val htmlText5 = "<ol>" +
                "<li>This contract will automatically terminate at the end of the rental period.</li>" +
                "<li>The First Party has the right to terminate this contract early if the Second Party violates any terms of this contract.</li>" +
                "</ol>"

        binding.tvArticle5.text = Html.fromHtml(htmlText5, Html.FROM_HTML_MODE_LEGACY)

        val htmlText6 = "<ol>" +
                "<li>Any disputes arising from the implementation of this contract will be resolved through mutual discussion between both parties.</li>" +
                "<li>If a mutual agreement cannot be reached, the dispute will be resolved through legal channels in [City].</li>" +
                "</ol>"

        binding.tvArticle6.text = Html.fromHtml(htmlText6, Html.FROM_HTML_MODE_LEGACY)

        val htmlText7 = "<ol>" +
                "<li>This contract is made in two copies, each copy having the same legal force, and is signed by both parties.</li>" +
                "<li>By signing this contract, both parties declare that they understand and agree to all the terms contained herein.</li>" +
                "</ol>"

        binding.tvArticle7.text = Html.fromHtml(htmlText7, Html.FROM_HTML_MODE_LEGACY)
    }

    private fun setupAction() {
        binding.btnArrowBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }

        binding.btnGenerateToken.setOnClickListener {
            val writer = QRCodeWriter()
            try {
                val bitMatrix = writer.encode(name , BarcodeFormat.QR_CODE, 128, 128)
                val width = bitMatrix.width
                val height = bitMatrix.height
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                for(x in 0 until width) {
                    for (y in 0 until  height) {
                        bmp.setPixel(x, y, if(bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                    }
                }
                binding.ivBarcode.visibility = View.VISIBLE
                binding.btnGenerateToken.visibility = View.GONE
                binding.ivBarcode.setImageBitmap(bmp)
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }

        binding.btnContinuePayment.setOnClickListener {
            rentViewModel.newTransaction(idRent = idBattery).observe(this) {
                it?.let { data ->
                    when(data) {
                        is ApiResult.Loading -> {
                            binding.btnContinuePayment.visibility = View.GONE
                            binding.ltLoading.visibility = View.VISIBLE
                        }

                        ApiResult.Empty -> {

                        }

                        is ApiResult.Error -> {
                            binding.btnContinuePayment.visibility = View.VISIBLE
                            binding.ltLoading.visibility = View.GONE
                            Toast.makeText(this, data.message, Toast.LENGTH_SHORT).show()
                        }

                        is ApiResult.Success -> {
                            Toast.makeText(this, data.data.status, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@ContractActivity, PaymentMethodActivity::class.java)
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

    companion object {
        const val PAYMENT_PRICE = "payment_price"
        const val BATTERY_ID = "battery_id"
    }
}