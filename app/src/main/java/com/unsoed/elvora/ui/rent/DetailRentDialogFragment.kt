package com.unsoed.elvora.ui.rent

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unsoed.elvora.databinding.FragmentDetailRentDialogListDialogBinding
import com.unsoed.elvora.ui.sumpayment.SummaryPaymentActivity

class DetailRentDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentDetailRentDialogListDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDetailRentDialogListDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val price = arguments?.getString(PRICE)
        val priceInt = arguments?.getDouble(PRICE_INT, 0.0)
        val type = arguments?.getString(TYPE)
        val desc = arguments?.getString(DESC)
        val name = arguments?.getString(NAME)
        val id = arguments?.getString(ID)
        val capacity = arguments?.getInt(CAPACITY, 0)

        binding.listDetail.text = "• ${capacity}Ah Battery Capacity\n• Battery Management Dashboard\n• Replacement Guarantee "
        binding.tvBatteryType.text = type
        binding.tvDetailDesc.text = desc
        binding.tvDialogBatteryPrice.text = price

        binding.btnRent.setOnClickListener {
            val intent = Intent(requireContext(), SummaryPaymentActivity::class.java)
            intent.putExtra(SummaryPaymentActivity.BATTERY_TYPE, type)
            intent.putExtra(SummaryPaymentActivity.BATTERY_RENT_ID, id)
            intent.putExtra(SummaryPaymentActivity.BATTERY_DESC, desc)
            intent.putExtra(SummaryPaymentActivity.BATTERY_PRICE, price)
            intent.putExtra(SummaryPaymentActivity.BATTERY_PRICE_INT, priceInt)
            intent.putExtra(SummaryPaymentActivity.FULL_NAME, name)
            startActivity(intent)
        }
    }


    companion object {
        const val TAG = "ModalDetailRentBottomSheet"
        const val PRICE = "price"
        const val PRICE_INT = "price_int"
        const val CAPACITY = "capacity"
        const val TYPE = "type"
        const val DESC = "desc"
        const val NAME = "name"
        const val ID = "id"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}