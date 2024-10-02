package com.unsoed.elvora.ui.subs

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
import com.unsoed.elvora.adapter.SubsAdapter
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.data.response.getSubs.AllSubsriptionsItem
import com.unsoed.elvora.databinding.ActivityTransactionBinding
import com.unsoed.elvora.helper.SubsModelFactory

class TransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionBinding
    private val subsViewModel: SubsViewModel by viewModels {
        SubsModelFactory.getInstance(this)
    }
    private var listSubs: List<AllSubsriptionsItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.btnArrowBack.setOnClickListener {
            OnBackPressedDispatcher().onBackPressed()
            finish()
        }

        subsViewModel.getAllSubs().observe(this) {
            it?.let { data ->
                when(data) {
                    is ApiResult.Loading -> {
                        binding.ltLoading.visibility = View.VISIBLE
                    }

                    ApiResult.Empty -> {

                    }

                    is ApiResult.Error -> {
                        Toast.makeText(this, data.message, Toast.LENGTH_SHORT).show()
                        binding.ltLoading.visibility = View.GONE
                    }

                    is ApiResult.Success -> {
                        listSubs = data.data.allSubscriptions
                        binding.ltLoading.visibility = View.GONE
                        listSubs?.let { subs ->
                            setupRecyclerView(subs)
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView(listSubs: List<AllSubsriptionsItem>) {
        if(listSubs.isNotEmpty()) {
            binding.rvTransaction.layoutManager = LinearLayoutManager(this)
            binding.rvTransaction.setHasFixedSize(true)
            binding.rvTransaction.adapter = SubsAdapter(listSubs.take(3))
        } else {
            binding.rvTransaction.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}