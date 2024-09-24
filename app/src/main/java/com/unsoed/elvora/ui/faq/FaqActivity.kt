package com.unsoed.elvora.ui.faq

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unsoed.elvora.R
import com.unsoed.elvora.databinding.ActivityFaqBinding

class FaqActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFaqBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnAccVerif.setOnClickListener {
            val intent = Intent(this, AccountVerificationActivity::class.java)
            startActivity(intent)
        }

        binding.btnPayment.setOnClickListener {
            Toast.makeText(this, "The related article has not been published yet.", Toast.LENGTH_SHORT).show()
        }
        binding.btnComplaint.setOnClickListener {
            Toast.makeText(this, "The related article has not been published yet.", Toast.LENGTH_SHORT).show()
        }
        binding.btnCustomer.setOnClickListener {
            Toast.makeText(this, "The related article has not been published yet.", Toast.LENGTH_SHORT).show()
        }
        binding.btnRefund.setOnClickListener {
            Toast.makeText(this, "The related article has not been published yet.", Toast.LENGTH_SHORT).show()
        }

        binding.btnArrowBack.setOnClickListener {
            OnBackPressedDispatcher().onBackPressed()
            finish()
        }
    }
}