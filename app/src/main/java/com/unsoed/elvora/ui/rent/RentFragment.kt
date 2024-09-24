package com.unsoed.elvora.ui.rent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.unsoed.elvora.R
import com.unsoed.elvora.data.UserModel
import com.unsoed.elvora.databinding.FragmentRentBinding
import com.unsoed.elvora.helper.RentModelFactory

class RentFragment : Fragment() {

    private var _binding: FragmentRentBinding? = null
    private val binding get() = _binding!!
    private val rentalViewModel: RentViewModel by viewModels {
        RentModelFactory.getInstance(requireContext())
    }
    private var userModel: UserModel? = null
    private var isPremium = false
    private var premiumDone = false
    private var userDone = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("RentFragment", "InitViewFragment")
        rentalViewModel.getTierUser().observe(viewLifecycleOwner) {
            it?.let { data ->
                isPremium = data
                premiumDone = true
            }
        }

        rentalViewModel.getUserSession().observe(viewLifecycleOwner) {
            Log.d("RentFragment", "getSession")
            it?.let {
                userModel = it
                userDone = true
            }
        }

        if(userDone && premiumDone) {
            val adapter = SectionsPagerAdapter(requireActivity())
            userModel?.let {
                adapter.userModel = UserModel(email = it.email, fullName = it.fullName, token = it.token)
            }
            adapter.premium = isPremium
            binding.vpRental.adapter = adapter
            TabLayoutMediator(binding.tabLayout, binding.vpRental) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}