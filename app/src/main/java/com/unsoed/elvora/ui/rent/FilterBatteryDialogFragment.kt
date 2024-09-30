package com.unsoed.elvora.ui.rent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unsoed.elvora.databinding.FragmentFilterBatteryDialogBinding

class FilterBatteryDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentFilterBatteryDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var onClickedButton: OnButtonFilterClicked
    private var listFilter: ArrayList<String> = arrayListOf()

    fun clickButton(onClickedButton: OnButtonFilterClicked) {
        this.onClickedButton = onClickedButton
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBatteryDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filter = arguments?.getStringArrayList(EXTRA_DATA)
        if (filter != null) {
            listFilter = filter
        }

        binding.btnApplyFilter.setOnClickListener {
            onClickedButton.onButtonClicked(listFilter)
            dismiss()
        }

        binding.btnCbTypeGesits.isChecked = listFilter.contains("Gesits")
        binding.btnCbTypeSelis.isChecked = listFilter.contains("Selis")
        binding.btnCbTypeAlva.isChecked = listFilter.contains("Alva")

        binding.btnCbTypeGesits.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                binding.btnCbTypeGesits.isChecked = true
                listFilter.add("Gesits")
            } else {
                binding.btnCbTypeGesits.isChecked = false
                listFilter.remove("Gesits")
            }
        }

        binding.btnCbTypeSelis.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                binding.btnCbTypeSelis.isChecked = true
                listFilter.add("Selis")
            } else {
                binding.btnCbTypeSelis.isChecked = false
                listFilter.remove("Selis")
            }
        }

        binding.btnCbTypeAlva.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                binding.btnCbTypeAlva.isChecked = true
                listFilter.add("Alva")
            } else {
                binding.btnCbTypeAlva.isChecked = false
                listFilter.remove("Alva")
            }
        }
    }

    interface OnButtonFilterClicked {
        fun onButtonClicked(value: ArrayList<String>)
    }

    companion object {
        const val TAG = "FilterDialogFragment"
        const val EXTRA_DATA = "extra_data"
    }

}