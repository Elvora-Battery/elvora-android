package com.unsoed.elvora.ui.subs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.unsoed.elvora.R
import com.unsoed.elvora.adapter.SubsAdapter
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.data.response.subs.ActiveSubsciption
import com.unsoed.elvora.data.response.subs.AllSubsriptionsItem
import com.unsoed.elvora.databinding.FragmentSubsBinding
import com.unsoed.elvora.helper.SubsModelFactory
import com.unsoed.elvora.helper.formatDatePlusOneMonth
import com.unsoed.elvora.ui.rent.RentalActivity

class SubsFragment : Fragment() {

    private var _binding: FragmentSubsBinding? = null
    private val binding get() = _binding!!
    private val subsViewModel: SubsViewModel by viewModels {
        SubsModelFactory.getInstance(requireContext())
    }
    private var listSubs: List<AllSubsriptionsItem>? = null
    private var activeSubs: ActiveSubsciption? = null

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
        binding.btnSubsNow.setOnClickListener {
            val intent = Intent(requireContext(), RentalActivity::class.java)
            intent.putExtra(RentalActivity.LIST_RENTAL, activeSubs)
            startActivity(intent)
        }

        binding.btnSeeAll.setOnClickListener {
            Toast.makeText(requireContext(), "The feature is still under development, please try again later.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSubsInformation() {
        subsViewModel.getAllSubs().observe(viewLifecycleOwner) {
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
                        listSubs = data.data.allSubsriptions
                        activeSubs = data.data.activeSubsciption
                        listSubs?.let { subs ->
                            setupRecyclerView(subs)
                        }

                        if(activeSubs != null) {
                            setupActiveSubs(activeSubs!!)
                            binding.tvEmptySubs.visibility = View.GONE
                            binding.btnSubsNow.visibility = View.VISIBLE
                            binding.btnArrowNext.visibility = View.VISIBLE
                            binding.divider.visibility = View.VISIBLE
                        } else {
                            binding.tvEmptySubs.visibility = View.VISIBLE
                            binding.btnSubsNow.visibility = View.GONE
                            binding.btnArrowNext.visibility = View.GONE
                            binding.divider.visibility = View.GONE
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
            tvNumberSumary.text = listSubs?.size.toString()
            tvNumberSumary.setTextColor(requireActivity().getColor(R.color.blue_container2))
            tvTitleSumary.setTextColor(requireActivity().getColor(R.color.blue_container2))
            cvSummary.background = shapeDrawable2
        }
    }

    private fun setupActiveSubs(activeSubs: ActiveSubsciption) {
        binding.tvSubsIdBattery.text ="EV${activeSubs.id}"
        binding.tvSubsIdBatteryType.text = if(activeSubs.rentTypeId == 1) "72V 20Ah Battery" else "72V 40Ah Battery"
        binding.tvSubsMotorcycleName.text = activeSubs.batteryName
        binding.tvSubsEndDate.text = "Ends on ${activeSubs.expirationDate.toString()}"
        binding.tvSubsCountDay.text = "31"
        binding.tvSubsEndMonth.text = formatDatePlusOneMonth(activeSubs.createdAt!!)
    }

    private fun setupRecyclerView(listSubs: List<AllSubsriptionsItem>) {
        if(listSubs.isNotEmpty()) {
            binding.rvSubs.layoutManager = LinearLayoutManager(requireContext())
            binding.rvSubs.setHasFixedSize(true)
            binding.rvSubs.adapter = SubsAdapter(listSubs)
            binding.tvEmptyList.visibility = View.GONE
        } else {
            binding.rvSubs.visibility = View.GONE
            binding.tvEmptyList.visibility = View.VISIBLE
        }
    }

}