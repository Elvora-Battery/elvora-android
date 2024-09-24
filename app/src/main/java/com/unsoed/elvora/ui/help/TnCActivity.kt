package com.unsoed.elvora.ui.help

import android.os.Bundle
import android.text.Html
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unsoed.elvora.R
import com.unsoed.elvora.databinding.ActivityTnCactivityBinding

class TnCActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTnCactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTnCactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val htmlText1 = "<ul>" +
                "<li>ELVORA: The EV battery rental and management application owned and operated by PT. Elvora.</li>" +
                "<li>User: An individual or entity that uses the ELVORA app.</li>" +
                "<li>UService: The provision of EV batteries for rent and battery management via the ELVORA app.</li>" +
                "</ul>"

        binding.tvTnc1.text = Html.fromHtml(htmlText1, Html.FROM_HTML_MODE_LEGACY)

        val htmlText2 = "<ul>" +
                "<li>The ELVORA app is only available for individuals 18 years and older who own an electric vehicle.</li>" +
                "<li>Users must register and create an account to access the services available on ELVORA.</li>" +
                "<li>Users are responsible for all activities that occur under their account and must maintain the confidentiality of their login information.</li>" +
                "</ul>"

        binding.tvTnc2.text = Html.fromHtml(htmlText2, Html.FROM_HTML_MODE_LEGACY)

        val htmlText3 = "<ul>" +
                "<li>ELVORA offers EV battery rental services that users can utilize based on availability.</li>" +
                "<li>Users must return rented batteries in the same condition as received, except for normal wear and tear.</li>" +
                "<li>ELVORA reserves the right to charge additional fees for late returns, damages, or loss of batteries.</li>" +
                "</ul>"

        binding.tvTnc3.text = Html.fromHtml(htmlText3, Html.FROM_HTML_MODE_LEGACY)

        val htmlText4 = "<ul>" +
                "<li>The app also provides battery management features, allowing users to monitor their battery status, including capacity, location, and usage history.</li>" +
                "<li>Data collected through this feature is used to improve the service and provide recommendations to users.</li>" +
                "</ul>"

        binding.tvTnc4.text = Html.fromHtml(htmlText4, Html.FROM_HTML_MODE_LEGACY)

        val htmlText5 = "<ul>" +
                "<li>Users agree to pay all fees associated with battery rentals through the payment methods provided in the app.</li>" +
                "<li>All fees charged are final and non-refundable unless otherwise required by applicable law.</li>" +
                "</ul>"

        binding.tvTnc5.text = Html.fromHtml(htmlText5, Html.FROM_HTML_MODE_LEGACY)

        val htmlText6 = "<ul>" +
                "<li>ELVORA is not liable for any damages or losses arising from the use of the app or services, including but not limited to data loss, vehicle damage, or personal injury.</li>" +
                "<li>The app and services are provided \"as is\" without any warranties.</li>" +
                "</ul>"

        binding.tvTnc6.text = Html.fromHtml(htmlText6, Html.FROM_HTML_MODE_LEGACY)

        val htmlText7 = "<ul>" +
                "<li>ELVORA reserves the right to modify these Terms & Conditions at any time without prior notice. Changes will take effect immediately upon being posted in the app.</li>" +
                "<li>Users are responsible for reviewing the Terms & Conditions periodically. Continued use of the app after changes are made constitutes acceptance of the new terms.</li>" +
                "</ul>"

        binding.tvTnc7.text = Html.fromHtml(htmlText7, Html.FROM_HTML_MODE_LEGACY)

        val htmlText8 = "<ul>" +
                "<li>These Terms & Conditions are governed by the laws of [Country/State Name], and any disputes arising will be resolved in the competent courts of [City/Country].</li>" +
                "</ul>"

        binding.tvTnc8.text = Html.fromHtml(htmlText8, Html.FROM_HTML_MODE_LEGACY)

        val htmlText9 = "<ul>" +
                "<li>ELVORA reserves the right to suspend or terminate a user’s account without notice if there is a violation of these Terms & Conditions.</li>" +
                "</ul>"

        binding.tvTnc9.text = Html.fromHtml(htmlText9, Html.FROM_HTML_MODE_LEGACY)

        val htmlText10 = "<ul>" +
                "<li>If you have any questions or complaints regarding these Terms & Conditions or the use of the ELVORA app, you can contact us at [Email Address/Phone Number].</li>" +
                "</ul>"

        binding.tvTnc10.text = Html.fromHtml(htmlText10, Html.FROM_HTML_MODE_LEGACY)
    }
}