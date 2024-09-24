package com.unsoed.elvora.ui.verification

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unsoed.elvora.MainActivity
import com.unsoed.elvora.R
import com.unsoed.elvora.databinding.ActivityCompleteVerificationBinding
import com.unsoed.elvora.ui.auth.LoginActivity

class CompleteVerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompleteVerificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCompleteVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val state = intent.getStringExtra(STATE)

        if(state == "password") {
            binding.tvComplete.text = "Your account has been successfully created"
            binding.btnContinue.setOnClickListener {
                val intent = Intent(this@CompleteVerificationActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        } else {
            binding.tvComplete.text = "Verification of your e-KTP and face have finished"
            binding.btnContinue.setOnClickListener {
                val intent = Intent(this@CompleteVerificationActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    companion object {
        const val STATE = "state"
    }
}