package com.unsoed.elvora.ui.rent

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.unsoed.elvora.adapter.ActivityAdapter
import com.unsoed.elvora.adapter.RentAdapter
import com.unsoed.elvora.data.Rental
import com.unsoed.elvora.data.UserModel
import com.unsoed.elvora.databinding.FragmentRentalBinding
import com.unsoed.elvora.dummy.activityDataList
import com.unsoed.elvora.dummy.rentalDataList

class RentalFragment : Fragment() {

    private var _binding: FragmentRentalBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRentalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val index = arguments?.getInt(TAB_INDEX, 0)
        val dataUser = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable(EXTRA_DATA, UserModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(EXTRA_DATA)
        }

        Log.d("RentFragment", dataUser.toString())

        if(index == 1) {
            val rentAdapter = RentAdapter(rentalDataList)
            binding.rvRental.adapter = rentAdapter
            binding.rvRental.layoutManager = LinearLayoutManager(requireContext())
            binding.rvRental.hasFixedSize()

            rentAdapter.onItemClickCallback(object: RentAdapter.OnItemClick {
                override fun onClick(data: Rental) {
                    val modalBottomSheet  = RentDialogFragment()
                    modalBottomSheet.show(parentFragmentManager, RentDialogFragment.TAG)
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
                        putString(DetailRentDialogFragment.NAME, dataUser?.fullName)
                    }
                }
            })
        } else {
            val activityAdapter = ActivityAdapter(activityDataList)
            binding.rvRental.adapter = activityAdapter
            binding.rvRental.layoutManager = LinearLayoutManager(requireContext())
            binding.rvRental.hasFixedSize()
        }
    }

    companion object {
        const val TAB_INDEX = "tab_index"
        const val EXTRA_DATA = "extra_data"
    }

}