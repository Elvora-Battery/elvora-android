package com.unsoed.elvora.ui.rent

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment = RentalFragment()
        fragment.arguments = Bundle().apply {
            putInt(RentalFragment.TAB_INDEX, position + 1)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}