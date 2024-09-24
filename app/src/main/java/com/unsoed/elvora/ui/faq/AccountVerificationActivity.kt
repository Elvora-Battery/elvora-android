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
import com.unsoed.elvora.databinding.ActivityAccountVerificationBinding
import com.unsoed.elvora.ui.profile.ArticleActivity

class AccountVerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountVerificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAccountVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnAv1.setOnClickListener {
            val intent = Intent(this, ArticleActivity::class.java)
            startActivity(intent)
        }
        binding.btnAv2.setOnClickListener {
            Toast.makeText(this, "The related article has not been published yet.", Toast.LENGTH_SHORT).show()
        }
        binding.btnAv3.setOnClickListener {
            Toast.makeText(this, "The related article has not been published yet.", Toast.LENGTH_SHORT).show()
        }
        binding.btnAv4.setOnClickListener {
            Toast.makeText(this, "The related article has not been published yet.", Toast.LENGTH_SHORT).show()
        }
        binding.btnAv5.setOnClickListener {
            Toast.makeText(this, "The related article has not been published yet.", Toast.LENGTH_SHORT).show()
        }

        binding.btnArrowBack.setOnClickListener {
            OnBackPressedDispatcher().onBackPressed()
            finish()
        }
    }
}