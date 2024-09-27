package com.unsoed.elvora.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unsoed.elvora.R
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.databinding.ActivityVerifyEmailBinding
import com.unsoed.elvora.helper.ViewModelFactory

class VerifyEmailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifyEmailBinding
    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVerifyEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val email = intent.getStringExtra(EMAIL_REGISTER)
        binding.tvVerifyDesc.text = "Enter the 6-digit OTP code sent to your email ($email)"

        binding.btnVerifyCode.setOnClickListener {
            val codeOtp = binding.etInputCode.editText?.text.toString().trim()
            if (email != null) {
                authViewModel.verifyOTP(email, codeOtp).observe(this) {
                    it?.let { data ->
                        when(data) {
                            is ApiResult.Loading -> {
                                binding.btnVerifyCode.visibility = View.GONE
                                binding.ltLoading.visibility = View.VISIBLE
                            }

                            ApiResult.Empty -> {

                            }

                            is ApiResult.Error -> {
                                binding.btnVerifyCode.visibility = View.VISIBLE
                                binding.ltLoading.visibility = View.GONE
                                Toast.makeText(this, data.message, Toast.LENGTH_SHORT).show()
                            }

                            is ApiResult.Success -> {
                                Toast.makeText(this, "Verify success", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@VerifyEmailActivity, SetPasswordActivity::class.java)
                                intent.putExtra(SetPasswordActivity.EMAIL, email)
                                startActivity(intent)
                            }
                        }
                    }
                }
            }
        }

        binding.btnResend.setOnClickListener {
            Toast.makeText(this, "Cek email Anda", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val EMAIL_REGISTER = "email_register"
    }
}