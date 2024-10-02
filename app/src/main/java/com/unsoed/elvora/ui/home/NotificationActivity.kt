package com.unsoed.elvora.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.unsoed.elvora.R
import com.unsoed.elvora.adapter.NotificationAdapter
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.data.response.notification.DataItem
import com.unsoed.elvora.databinding.ActivityNotificationBinding
import com.unsoed.elvora.helper.HomeModelFactory

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding
    private val homeViewModel: HomeViewModel by viewModels {
        HomeModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        homeViewModel.getNotificationData().observe(this) {
            it?.let { response ->
                when(response) {
                    is ApiResult.Loading -> {
                        binding.ltLoading.visibility = View.VISIBLE
                    }

                    is ApiResult.Success -> {
                        val listNotification: List<DataItem> = response.data
                        if(listNotification.isNotEmpty()) {
                            val notificationAdapter = NotificationAdapter(listNotification)
                            binding.rvNotification.adapter = notificationAdapter
                            binding.rvNotification.layoutManager = LinearLayoutManager(this)
                            binding.rvNotification.hasFixedSize()
                            binding.layoutEmpty.visibility = View.GONE
                        } else {
                            binding.rvNotification.visibility = View.VISIBLE
                            binding.layoutEmpty.visibility = View.VISIBLE
                        }
                    }

                    is ApiResult.Error -> {
                        Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
                    }

                    else -> { }
                }
            }
        }
        val adapter = NotificationAdapter(emptyList())
        binding.rvNotification.layoutManager = LinearLayoutManager(this)
        binding.rvNotification.setHasFixedSize(true)
        binding.rvNotification.adapter = adapter


        binding.btnArrowBack.setOnClickListener {
            OnBackPressedDispatcher().onBackPressed()
            finish()
        }
    }
}