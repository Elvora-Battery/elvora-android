package com.unsoed.elvora.ui.rent

import android.os.Bundle
import android.text.Html
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unsoed.elvora.R
import com.unsoed.elvora.databinding.ActivityContractBinding

class ContractActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContractBinding

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
}