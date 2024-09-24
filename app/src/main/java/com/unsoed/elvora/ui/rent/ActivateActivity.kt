package com.unsoed.elvora.ui.rent

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unsoed.elvora.MainActivity
import com.unsoed.elvora.R
import com.unsoed.elvora.databinding.ActivityActivateBinding

class ActivateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityActivateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityActivateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnArrowBack.setOnClickListener {
            OnBackPressedDispatcher().onBackPressed()
            finish()
        }

        binding.btnActivate.setOnClickListener {
            val token = binding.etInputTokenNumber.editText?.text.toString().trim()
            when {
                token.isEmpty() -> {
                    binding.etInputTokenNumber.error = "Please fill out this field"
                }

                else -> {
                    val intent = Intent(this@ActivateActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                    Toast.makeText(this, "Token Anda : $token", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}