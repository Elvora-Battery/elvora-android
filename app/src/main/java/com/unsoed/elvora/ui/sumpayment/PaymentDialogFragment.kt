package com.unsoed.elvora.ui.sumpayment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.data.Payment
import com.unsoed.elvora.databinding.FragmentPaymentDialogBinding
import com.unsoed.elvora.helper.RentModelFactory
import com.unsoed.elvora.ui.rent.RentViewModel
import com.unsoed.elvora.ui.subs.DetailTransactionActivity
import java.time.LocalDateTime

class PaymentDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentPaymentDialogBinding? = null
    private val binding get() = _binding!!
    private val rentViewModel: RentViewModel by viewModels {
        RentModelFactory.getInstance(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataPayment = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable(EXTRA_DATA, Payment::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(EXTRA_DATA)
        }

        val idTransaction = arguments?.getInt(ID_TRANSACTION, 0)
        val typeBattery = arguments?.getString(TYPE_BATTERY)
        val idBattery = arguments?.getString(ID_BATTERY)
        val total = arguments?.getString(TOTAL_PRICE)

        dataPayment?.let {
            binding.apply {
                tvNamePayment.text = dataPayment.name
                tvNumberPayment.text = dataPayment.number
                tvPaymentDesc.text = dataPayment.desc
                ivPayment.setImageDrawable(AppCompatResources.getDrawable(requireContext(), dataPayment.image))
            }
        }

        binding.btnConfirmPayment.setOnClickListener {
            rentViewModel.paidTransaction(idTransaction.toString()).observe(viewLifecycleOwner) {
                it?.let { data ->
                    when(data) {
                        is ApiResult.Loading -> {
                            binding.ltLoading.visibility = View.VISIBLE
                            binding.btnConfirmPayment.visibility = View.GONE
                        }

                        ApiResult.Empty -> {

                        }

                        is ApiResult.Error -> {
                            binding.ltLoading.visibility = View.GONE
                            binding.btnConfirmPayment.visibility = View.VISIBLE
                            Toast.makeText(requireContext(), data.message, Toast.LENGTH_SHORT).show()
                        }

                        is ApiResult.Success -> {
                            Toast.makeText(requireContext(), "Confirmed Payment", Toast.LENGTH_SHORT).show()
                            Log.d(TAG, data.data.token.toString())
                            val intent = Intent(requireContext(), DetailTransactionActivity::class.java)
                            intent.putExtra(DetailTransactionActivity.BATTERY_TYPE, typeBattery)
                            intent.putExtra(DetailTransactionActivity.ID_BATTERY_TYPE, idBattery)
                            intent.putExtra(DetailTransactionActivity.STATUS_TRANSACTION, "Paid Off")
                            intent.putExtra(DetailTransactionActivity.ID_TRANSACTION, idTransaction.toString())
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                intent.putExtra(DetailTransactionActivity.DATE_TRANSACTION, LocalDateTime.now().toString())
                            }
                            intent.putExtra(DetailTransactionActivity.PAYMENT_TRANSACTION, dataPayment?.name)
                            intent.putExtra(DetailTransactionActivity.TOTAL_PRICE, total)
                            intent.putExtra(DetailTransactionActivity.TOKEN, data.data.token)
                            startActivity(intent)
                        }
                    }
                }
            }
        }

        binding.btnCopyVa.setOnClickListener {
            val textToCopy = binding.tvNumberPayment.text.toString()

            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", textToCopy)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(requireContext(), "Text copied to clipboard", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val TAG = "ModalPaymentBottomSheet"
        const val EXTRA_DATA = "extra_data"
        const val ID_TRANSACTION = "id_transaction"
        const val TOTAL_PRICE = "total_price"
        const val TYPE_BATTERY = "type_battery"
        const val ID_BATTERY = "id_battery"
    }
}