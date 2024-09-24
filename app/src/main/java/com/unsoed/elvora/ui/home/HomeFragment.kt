package com.unsoed.elvora.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.unsoed.elvora.R
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.data.UserModel
import com.unsoed.elvora.data.response.home.Battery
import com.unsoed.elvora.data.response.home.Transaction
import com.unsoed.elvora.databinding.FragmentHomeBinding
import com.unsoed.elvora.helper.HomeModelFactory
import com.unsoed.elvora.ui.profile.ProfileActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels {
        HomeModelFactory.getInstance(requireContext())
    }
    private var userData: UserModel? = null
    private var isTierUserPremium: Boolean = false
    private var batteryData: Battery? = null
    private var transactionData: Transaction? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setupViewFragment()
    }

    private fun initView() {
        homeViewModel.getUserSession().observe(viewLifecycleOwner) {
            it?.let {
                binding.tvNameMember.text = it.fullName
                userData = it
            }
        }

        homeViewModel.getTierUser().observe(viewLifecycleOwner) {
            it?.let { isPremium ->
                binding.tvTierMember.text = if (isPremium) "Premium Member" else "Basic Member"
                isTierUserPremium = isPremium
            }
        }

        homeViewModel.getUserShipping().observe(viewLifecycleOwner) {
            it?.let { data ->
                if (data.address.isEmpty()) {
                    binding.tvMemberLocation.text = "Location is not set"
                } else {
                    binding.tvMemberLocation.text =
                        "${data.street}, ${data.village}, ${data.address}"
                }
            }
        }

        binding.ivMember.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            intent.putExtra(ProfileActivity.EXTRA_DATA, userData)
            intent.putExtra(ProfileActivity.EXTRA_PREMIUM, isTierUserPremium)
            startActivity(intent)
        }

    }

    private fun setupCardDriveTime() {
        val shapeAppearanceModel = ShapeAppearanceModel.builder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 20f)
            .setBottomRightCorner(CornerFamily.ROUNDED, 20f)
            .build()

        val shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)

        binding.cardTime.apply {
            tvTitleSumary.text = "Drive Time"
            tvNumberSumary.text = "-"
            tvSatuanSumary.text = "h"
            cvSummary.background = shapeDrawable
        }
    }

    private fun setupCardDistance() {
        val shapeAppearanceModel = ShapeAppearanceModel.builder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
            .build()

        val shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)

        binding.cardDistance.apply {
            tvTitleSumary.text = "Total Distance"
            tvNumberSumary.text = "-"
            tvSatuanSumary.text = "Km"
            cvSummary.background = shapeDrawable
        }
    }

    private fun setupCardConsumption() {
        val shapeAppearanceModel = ShapeAppearanceModel.builder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 20f)
            .setBottomLeftCorner(CornerFamily.ROUNDED, 20f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
            .build()

        val shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)

        binding.cardConsumption.apply {
            tvTitleSumary.text = "Usage Rate"
            tvNumberSumary.text = "${batteryData?.dayaDigunakan}"
            tvSatuanSumary.text = "KwH"
            cvSummary.background = shapeDrawable
        }
    }

    private fun setupCardTemperature() {
        binding.cardTempMonitoring.apply {
            ivCardMonitoring.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.icon_temp
                )
            )
            titleCard.text = "Temperature"
            tvCardPercentage.text = "${batteryData?.suhu}°C"
            tvCardEstimate.text = "Good Condition"
            cvMonitoring.setCardBackgroundColor(requireContext().getColor(R.color.green_container))
        }
    }

    private fun setupCardBattery() {
        binding.cardBatteryMonitoring.apply {
            ivCardMonitoring.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.icon_pencil_battery
                )
            )
            titleCard.text = "Daya"
            tvCardPercentage.text = batteryData?.daya
            tvCardEstimate.text = "Est. distance 95km"
            cvMonitoring.setCardBackgroundColor(requireContext().getColor(R.color.blue_container))
        }
    }

    private fun setupViewFragment() {
        homeViewModel.getDashboardData().observe(viewLifecycleOwner) {
            it?.let { data ->
                when (data) {
                    is ApiResult.Loading -> {

                    }

                    ApiResult.Empty -> {

                    }

                    is ApiResult.Error -> {
                        Toast.makeText(requireContext(), data.message, Toast.LENGTH_SHORT).show()
                        binding.cvLayoutDashboard.visibility = View.INVISIBLE
                        binding.tvEmptySubs.visibility = View.VISIBLE
                        setupCardDriveTime()
                        setupCardBattery()
                        setupCardConsumption()
                        setupCardDistance()
                        setupCardTemperature()
                    }

                    is ApiResult.Success -> {
                        if (data.data.transaction != null && data.data.battery != null) {
                            batteryData = data.data.battery
                            transactionData = data.data.transaction
                            setupCardBattery()
                            setupCardTemperature()
                            setupCardConsumption()
                            setupCardDistance()
                            setupCardDriveTime()
                            binding.apply {
                                tvIdBattery.text = "EV${data.data.transaction.id}"
                                tvIdBatteryType.text =
                                    if (data.data.transaction.rentTypeId == 1) "72V 20Ah Battery" else "72V 40Ah Battery"
                                tvMotorcycleName.text = data.data.transaction.batteryName
                                ivMember.setImageDrawable(
                                    AppCompatResources.getDrawable(
                                        requireContext(),
                                        R.drawable.avatar3
                                    )
                                )
                            }
                        } else {
                            binding.cvLayoutDashboard.visibility = View.INVISIBLE
                            binding.tvEmptySubs.visibility = View.VISIBLE
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
}