package com.unsoed.elvora.ui.subs

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.unsoed.elvora.R
import com.unsoed.elvora.adapter.SubsAdapter
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.data.response.getSubs.ActiveSubscription
import com.unsoed.elvora.data.response.getSubs.AllSubsriptionsItem
import com.unsoed.elvora.databinding.FragmentSubsBinding
import com.unsoed.elvora.helper.SubsModelFactory
import com.unsoed.elvora.helper.formatDate
import com.unsoed.elvora.helper.formatDatePlusOneMonth
import com.unsoed.elvora.ui.rent.RentalActivity

class SubsFragment : Fragment() {

    private var _binding: FragmentSubsBinding? = null
    private val binding get() = _binding!!
    private val subsViewModel: SubsViewModel by viewModels {
        SubsModelFactory.getInstance(requireContext())
    }
    private var listSubs: List<AllSubsriptionsItem>? = null
    private var activeSubs: ActiveSubscription? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSubsInformation()
        setupAction()
    }

    private fun setupAction() {
        binding.btnArrowNext.setOnClickListener {
            val intent = Intent(requireContext(), RentalActivity::class.java)
            intent.putExtra(RentalActivity.LIST_RENTAL, activeSubs)
            startActivity(intent)
        }

        binding.btnSeeAll.setOnClickListener {
            val intent = Intent(requireContext(), TransactionActivity::class.java)
            startActivity(intent)
        }

        binding.btnSubsNow.setOnClickListener {

        }
    }

    private fun setupSubsInformation() {
        subsViewModel.getAllSubs().observe(viewLifecycleOwner) {
            it?.let { data ->
                when(data) {
                    is ApiResult.Loading -> {
                        binding.ltLoading.visibility = View.VISIBLE
                    }

                    ApiResult.Empty -> {

                    }

                    is ApiResult.Error -> {
                        Toast.makeText(requireContext(), data.message, Toast.LENGTH_SHORT).show()
                        binding.ltLoading.visibility = View.GONE
                    }

                    is ApiResult.Success -> {
                        binding.ltLoading.visibility = View.GONE
                        listSubs = data.data.allSubscriptions
                        activeSubs = data.data.activeSubscription
                        listSubs?.let { subs ->
                            setupRecyclerView(subs)
                        }
                        Log.d("SubsFragment", data.data.activeSubscription.toString())

                        val dataSubs = data.data.subs
                        dataSubs?.let { subs ->
                            binding.cvRemainSubs.tvNumberSumary.text = subs.remainingSubscription.toString()
                            binding.cvTotalSubs.tvNumberSumary.text = subs.totalSubscription.toString()
                            binding.tvSubsCountDay.text = subs.remainingTime.toString()
                        }

                        if(activeSubs?.id != null) {
                            setupActiveSubs(activeSubs!!)
                            binding.tvEmptySubs.visibility = View.GONE
                            binding.btnSubsNow.visibility = View.VISIBLE
                            binding.btnSubsNow.text = "Already Subscription"
                            binding.btnSubsNow.setTextColor(requireContext().getColor(R.color.green_text))
                            binding.btnSubsNow.backgroundTintList =  ColorStateList.valueOf(
                                ContextCompat.getColor(requireContext(), R.color.green_container2))
                            binding.btnArrowNext.visibility = View.VISIBLE
                            binding.divider.visibility = View.VISIBLE
                            binding.btnSeeAll.visibility = View.VISIBLE
                        } else {
                            Log.d("SubsFragment", "Data Subs Kosong")
                            binding.tvEmptySubs.visibility = View.VISIBLE
                            binding.ltEmpty.visibility = View.VISIBLE
                            binding.btnSeeAll.visibility = View.GONE
                            binding.btnSubsNow.visibility = View.GONE
                            binding.btnArrowNext.visibility = View.GONE
                            binding.divider.visibility = View.GONE
                            binding.tvDay.visibility = View.GONE
                            binding.view.visibility = View.GONE
                            binding.cvTotalSubs.tvNumberSumary.text = "0"
                        }
                    }
                }
            }
        }
        val shapeAppearanceModel = ShapeAppearanceModel.builder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 20f)
            .setBottomRightCorner(CornerFamily.ROUNDED, 20f)
            .build()

        val shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)

        binding.cvRemainSubs.apply {
            tvTitleSumary.text = "Remaining Subs"
            tvNumberSumary.text = "-"
            tvNumberSumary.setTextColor(requireActivity().getColor(R.color.green_tint))
            tvTitleSumary.setTextColor(requireActivity().getColor(R.color.green_tint))
            cvSummary.background = shapeDrawable
        }

        val shapeAppearanceModel2 = ShapeAppearanceModel.builder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 20f)
            .setBottomLeftCorner(CornerFamily.ROUNDED, 20f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
            .build()

        val shapeDrawable2 = MaterialShapeDrawable(shapeAppearanceModel2)

        binding.cvTotalSubs.apply {
            tvTitleSumary.text = "Total Subs"
            tvNumberSumary.setTextColor(requireActivity().getColor(R.color.blue_container2))
            tvTitleSumary.setTextColor(requireActivity().getColor(R.color.blue_container2))
            cvSummary.background = shapeDrawable2
        }
    }

    private fun setupActiveSubs(activeSubs: ActiveSubscription) {
        val formatDate = activeSubs.expirationDate?.let { formatDate(it) }

        binding.tvSubsIdBattery.text ="EV${activeSubs.id}"
        binding.tvSubsIdBatteryType.text = if(activeSubs.rentTypeId == 1) "72V 20Ah Battery" else "72V 40Ah Battery"
        binding.tvSubsMotorcycleName.text = activeSubs.batteryName
        binding.tvSubsEndDate.text = "Ends on $formatDate"
        binding.tvSubsEndMonth.text = "Until ${formatDatePlusOneMonth(activeSubs.createdAt!!)}"
    }

    private fun setupRecyclerView(listSubs: List<AllSubsriptionsItem>) {
        if(listSubs.isNotEmpty()) {
            Log.d("SubsFragment", "List Ada")
            binding.rvSubs.layoutManager = LinearLayoutManager(requireContext())
            binding.rvSubs.setHasFixedSize(true)
            binding.rvSubs.adapter = SubsAdapter(listSubs.take(3))
            binding.tvEmptyList.visibility = View.GONE
            binding.ltEmptyList.visibility = View.GONE
        } else {
            Log.d("SubsFragment", "List Kosong")
            binding.rvSubs.visibility = View.GONE
            binding.ltEmptyList.visibility = View.VISIBLE
            binding.tvEmptyList.visibility = View.VISIBLE
        }
    }

}