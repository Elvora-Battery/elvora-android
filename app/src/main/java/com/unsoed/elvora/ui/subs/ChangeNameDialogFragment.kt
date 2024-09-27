package com.unsoed.elvora.ui.subs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.databinding.FragmentChangeNameDialogBinding
import com.unsoed.elvora.helper.SubsModelFactory

class ChangeNameDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentChangeNameDialogBinding? = null
    private val subsViewModel: SubsViewModel by viewModels {
        SubsModelFactory.getInstance(requireContext())
    }
    private var nameChangeListener: OnNameChangeListener? = null

    fun changeBatteryName(nameListener: OnNameChangeListener) {
        nameChangeListener = nameListener
    }

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangeNameDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = arguments?.getString(BATTERY_NAME)
        val idBattery = arguments?.getInt(BATTERY_ID, 0)

        name?.let {
            binding.etInputChangeName.editText?.setText(it)
        }

        binding.btnChangeName.setOnClickListener {
            val nameInput = binding.etInputChangeName.editText?.text.toString()
            if(name == nameInput) {
                dismiss()
            } else {
                if (idBattery != null) {
                    subsViewModel.changeNameBattery(id = idBattery, name = nameInput).observe(viewLifecycleOwner) {
                        it?.let { data ->
                            when(data) {
                                is ApiResult.Loading -> {

                                }

                                ApiResult.Empty -> {

                                }

                                is ApiResult.Error -> {
                                    Toast.makeText(requireContext(), data.message, Toast.LENGTH_SHORT).show()
                                }

                                is ApiResult.Success -> {
                                    Toast.makeText(requireContext(), data.data, Toast.LENGTH_SHORT).show()
                                    nameChangeListener?.onNameChanged(nameInput)
                                    dismiss()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnNameChangeListener {
        fun onNameChanged(newName: String)
    }

    companion object {
        const val TAG = "ModalChangeBottomSheet"
        const val BATTERY_NAME = "battery_name"
        const val BATTERY_ID = "battery_id"

    }
}