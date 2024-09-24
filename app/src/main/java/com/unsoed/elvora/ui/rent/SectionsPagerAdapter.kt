package com.unsoed.elvora.ui.rent

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.unsoed.elvora.data.UserModel

class SectionsPagerAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment) {

    var userModel = UserModel(email = "", fullName = "", token = "", premium = false)

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment = RentalFragment()
        fragment.arguments = Bundle().apply {
            putInt(RentalFragment.TAB_INDEX, position + 1)
            putParcelable(RentalFragment.EXTRA_DATA, userModel)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}