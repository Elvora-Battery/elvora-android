package com.unsoed.elvora.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.unsoed.elvora.R
import com.unsoed.elvora.data.UserModel
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
        setupCardBattery()
        setupCardTemperature()
        setupCardConsumption()
        setupCardDistance()
        setupCardDriveTime()
    }

    private fun initView() {
        homeViewModel.getUserSession().observe(viewLifecycleOwner) {
            it?.let {
                binding.tvNameMember.text = it.fullName
                userData = it
            }
        }

        binding.ivMember.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            intent.putExtra(ProfileActivity.EXTRA_DATA, userData)
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
            tvNumberSumary.text = "12"
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
            tvNumberSumary.text = "43"
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
            tvNumberSumary.text = "69"
            tvSatuanSumary.text = "KwH"
            cvSummary.background = shapeDrawable
        }
    }

    private fun setupCardTemperature() {
        binding.cardTempMonitoring.apply {
            ivCardMonitoring.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.icon_temp))
            titleCard.text = "Temperature"
            tvCardPercentage.text = "28°C"
            tvCardEstimate.text = "Good Condition"
            cvMonitoring.setCardBackgroundColor(requireContext().getColor(R.color.green_container))
        }
    }

    private fun setupCardBattery() {
        binding.cardBatteryMonitoring.apply {
            ivCardMonitoring.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.icon_pencil_battery))
            titleCard.text = "Battery"
            tvCardPercentage.text = "69%"
            tvCardEstimate.text = "Est. distance 95km"
            cvMonitoring.setCardBackgroundColor(requireContext().getColor(R.color.blue_container))
        }
    }

    private fun setupViewFragment() {
        binding.apply {
            tvTierMember.text = "Premium Member"
            tvIdBattery.text = "EV2001"
            tvIdBatteryType.text = "72V 40Ah Battery"
            tvMotorcycleName.text = "Motor EV200 Vision"
            tvMemberLocation.text = "Mandiraja, Banjarnegara, Jawa Tengah"
            ivMember.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.avatar3))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}