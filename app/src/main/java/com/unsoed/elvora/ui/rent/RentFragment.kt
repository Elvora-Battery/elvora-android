package com.unsoed.elvora.ui.rent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.unsoed.elvora.R
import com.unsoed.elvora.databinding.FragmentRentBinding

class RentFragment : Fragment() {

    private var _binding: FragmentRentBinding? = null
    private val binding get() = _binding!!

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

        val adapter = SectionsPagerAdapter(requireActivity())
        binding.vpRental.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.vpRental) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}