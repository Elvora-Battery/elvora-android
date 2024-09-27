package com.unsoed.elvora.ui.rent

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.unsoed.elvora.R
import com.unsoed.elvora.adapter.ListRentalAdapter
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.data.response.active.DataItem
import com.unsoed.elvora.databinding.ActivityRentalBinding
import com.unsoed.elvora.helper.SubsModelFactory
import com.unsoed.elvora.ui.subs.SubsViewModel

class RentalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRentalBinding
    private val subsViewModel: SubsViewModel by viewModels {
        SubsModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRentalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        subsViewModel.getActiveSubs().observe(this) {
            it?.let { response ->
                when (response) {
                    is ApiResult.Loading -> {

                    }

                    is ApiResult.Success -> {
                        val list = response.data
                        setupData(response.data)
                    }

                    is ApiResult.Error -> {
                        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                        Log.d(TAG, response.message)
                    }

                    ApiResult.Empty -> {

                    }
                }
            }
        }
    }

    private fun setupData(data: List<DataItem>) {
        if(data.isNotEmpty()) {
            binding.rvListActiveSubs.layoutManager = LinearLayoutManager(this)
            binding.rvListActiveSubs.setHasFixedSize(true)
            binding.rvListActiveSubs.adapter = ListRentalAdapter(data)
            binding.ltLoading.visibility = View.GONE
        } else {
            binding.rvListActiveSubs.visibility = View.GONE
            binding.ltLoading.visibility = View.VISIBLE
        }
    }


    companion object {
        private const val TAG = "RentalActivity"
        const val LIST_RENTAL = "list_rental"
    }
}