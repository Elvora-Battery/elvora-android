package com.unsoed.elvora.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unsoed.elvora.databinding.FragmentActivateDialogBinding
import com.unsoed.elvora.ui.rent.ActivateActivity

class ActivateDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentActivateDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivateDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnHasArrived.setOnClickListener {
            val intent = Intent(requireContext(), ActivateActivity::class.java)
            startActivity(intent)
        }

        binding.btnNoArrived.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ActivateBottomSheetDialog"
    }
}