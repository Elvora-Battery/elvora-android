package com.unsoed.elvora.ui.rent

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unsoed.elvora.MainActivity
import com.unsoed.elvora.R
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.databinding.ActivityActivateBinding
import com.unsoed.elvora.helper.RentModelFactory

class ActivateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityActivateBinding
    private val rentViewModel: RentViewModel by viewModels {
        RentModelFactory.getInstance(this)
    }

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
                    rentViewModel.publishToken(token).observe(this) {
                        it?.let { data ->
                            when(data) {
                                is ApiResult.Loading -> {
                                    binding.ltLoading.visibility = View.VISIBLE
                                    binding.btnActivate.visibility = View.GONE
                                }

                                ApiResult.Empty -> {

                                }

                                is ApiResult.Error -> {
                                    binding.ltLoading.visibility = View.GONE
                                    binding.btnActivate.visibility = View.VISIBLE
                                    Toast.makeText(this, data.message, Toast.LENGTH_SHORT).show()
                                }

                                is ApiResult.Success -> {
                                    val intent = Intent(this@ActivateActivity, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    finish()
                                    Toast.makeText(this, data.data, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}