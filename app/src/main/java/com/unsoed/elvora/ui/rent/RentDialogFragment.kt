package com.unsoed.elvora.ui.rent

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unsoed.elvora.databinding.FragmentRentDialogListDialogBinding
import com.unsoed.elvora.ui.verification.CardVerificationActivity

class RentDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentRentDialogListDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRentDialogListDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnVerifyAcc.setOnClickListener {
            val intent = Intent(requireContext(), CardVerificationActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ModalRentBottomSheet"
    }
}