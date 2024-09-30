package com.unsoed.elvora.ui.rent

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.unsoed.elvora.adapter.ActivityAdapter
import com.unsoed.elvora.adapter.ButtonFilterAdapter
import com.unsoed.elvora.adapter.RentAdapter
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.data.Rental
import com.unsoed.elvora.data.UserModel
import com.unsoed.elvora.databinding.FragmentRentalBinding
import com.unsoed.elvora.dummy.rentalDataList
import com.unsoed.elvora.helper.RentModelFactory
import com.unsoed.elvora.ui.sumpayment.SummaryPaymentActivity

class RentalFragment : Fragment() {

    private var _binding: FragmentRentalBinding? = null
    val binding get() = _binding!!
    private val rentViewModel: RentViewModel by viewModels {
        RentModelFactory.getInstance(requireContext())
    }
    private var userModel: UserModel? = null
    private var isPremium = false
    private var listFilter: ArrayList<String> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRentalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rentViewModel.getTierUser().observe(viewLifecycleOwner) {
            it?.let { data ->
                isPremium = data
            }
        }

        rentViewModel.getUserSession().observe(viewLifecycleOwner) {
            Log.d("RentFragment", "getSession")
            it?.let {
                userModel = it
            }
        }

        binding.btnFilter.setOnClickListener {
            val bottomSheet = FilterBatteryDialogFragment()
            bottomSheet.arguments = Bundle().apply {
                putStringArrayList(FilterBatteryDialogFragment.EXTRA_DATA, listFilter)
            }
            bottomSheet.show(parentFragmentManager, FilterBatteryDialogFragment.TAG)

            bottomSheet.clickButton(object: FilterBatteryDialogFragment.OnButtonFilterClicked {
                override fun onButtonClicked(value: ArrayList<String>) {
                    listFilter = value
                    setupRecyclerView()
                    setupRecyclerViewFilter()
                }
            })
        }

        val index = arguments?.getInt(TAB_INDEX, 0)

        if(index == 1) {
            binding.layoutFilter.visibility = View.VISIBLE
            setupRecyclerView()
        } else {
            binding.layoutFilter.visibility = View.GONE
            rentViewModel.getAllTransaction().observe(viewLifecycleOwner) {
                it?.let { response ->
                    when (response) {
                        is ApiResult.Loading -> {
                            binding.ltLoading.visibility = View.VISIBLE
                        }

                        ApiResult.Empty -> {

                        }

                        is ApiResult.Error -> {
                            Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                            binding.ltLoading.visibility = View.GONE
                        }

                        is ApiResult.Success -> {
                            binding.ltLoading.visibility = View.GONE
                            val activityData = response.data
                            if(activityData.isNotEmpty()) {
                                val activityAdapter = ActivityAdapter(activityData)
                                binding.rvRental.adapter = activityAdapter
                                binding.rvRental.layoutManager = LinearLayoutManager(requireContext())
                                binding.rvRental.hasFixedSize()
                                binding.tvEmptyList.visibility = View.GONE
                                binding.ltEmptyList.visibility = View.GONE
                            } else {
                                binding.ltEmptyList.visibility = View.VISIBLE
                                binding.rvRental.visibility = View.GONE
                                binding.tvEmptyList.visibility = View.VISIBLE
                            }

                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerViewFilter() {
        val filterAdapter = ButtonFilterAdapter(listFilter)
        binding.rvFilter.adapter = filterAdapter
        binding.rvFilter.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvRental.hasFixedSize()
    }

    private fun setupRecyclerView() {
        val rentAdapter = RentAdapter(getListByFilter(listFilter))
        binding.rvRental.adapter = rentAdapter
        binding.rvRental.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRental.hasFixedSize()

        rentAdapter.onItemClickCallback(object: RentAdapter.OnItemClick {
            override fun onClick(data: Rental) {
                if(isPremium) {
                    val intent = Intent(requireContext(), SummaryPaymentActivity::class.java)
                    intent.putExtra(SummaryPaymentActivity.BATTERY_TYPE, data.type)
                    intent.putExtra(SummaryPaymentActivity.BATTERY_RENT_ID, data.id.toString())
                    intent.putExtra(SummaryPaymentActivity.BATTERY_DESC, data.description)
                    intent.putExtra(SummaryPaymentActivity.BATTERY_PRICE, data.price)
                    intent.putExtra(SummaryPaymentActivity.BATTERY_PRICE_INT, data.priceInt)
                    intent.putExtra(SummaryPaymentActivity.FULL_NAME, userModel?.fullName)
                    startActivity(intent)
                } else {
                    val modalBottomSheet  = RentDialogFragment()
                    modalBottomSheet.show(parentFragmentManager, RentDialogFragment.TAG)
                }
            }
        })

        rentAdapter.onDetailItemClickCallback(object: RentAdapter.OnItemClick {
            override fun onClick(data: Rental) {
                val modalBottomSheet  = DetailRentDialogFragment()
                modalBottomSheet.show(parentFragmentManager, DetailRentDialogFragment.TAG)
                modalBottomSheet.arguments = Bundle().apply {
                    putString(DetailRentDialogFragment.ID, data.id.toString())
                    putString(DetailRentDialogFragment.PRICE, data.price)
                    putDouble(DetailRentDialogFragment.PRICE_INT, data.priceInt)
                    putString(DetailRentDialogFragment.DESC, data.description)
                    putString(DetailRentDialogFragment.TYPE, data.type)
                    putInt(DetailRentDialogFragment.CAPACITY, data.capacity)
                    putString(DetailRentDialogFragment.NAME, userModel?.fullName)
                    putBoolean(DetailRentDialogFragment.PREMIUM, isPremium)
                }
            }
        })
    }

    private fun getListByFilter(list: ArrayList<String>): List<Rental> {
        return if (list.isEmpty()) {
            rentalDataList
        } else {
            rentalDataList.filter { rental ->
                list.any { keyword ->
                    rental.filter == keyword
                }
            }
        }
    }

    companion object {
        const val TAB_INDEX = "tab_index"
    }

}