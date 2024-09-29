package com.unsoed.elvora.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
        setupCardDriveTime()
        setupCardBattery()
        setupCardConsumption()
        setupCardDistance()
        setupCardTemperature()
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
                        "${data.street}, ${data.village}"
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
            tvTitleSumary.text = "Arus"
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
            tvTitleSumary.text = "Tegangan"
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
            tvCardEstimate.text = "Est. distance -km"
            cvMonitoring.setCardBackgroundColor(requireContext().getColor(R.color.blue_container))
        }
    }

    private fun setupViewFragment() {
        homeViewModel.getDashboardData().observe(viewLifecycleOwner) {
            it?.let { response ->
                when (response) {
                    is ApiResult.Loading -> {
                        binding.ltLoading.visibility = View.VISIBLE
                    }

                    ApiResult.Empty -> {

                    }

                    is ApiResult.Error -> {
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                        binding.ltLoading.visibility = View.GONE
                        binding.cvLayoutDashboard.visibility = View.INVISIBLE
                        binding.tvEmptySubs.visibility = View.VISIBLE
                    }

                    is ApiResult.Success -> {
                        Log.d("HomeFragment", "Data : ${response.data}")
                        Log.d("HomeFragment", response.data.transaction.toString())
                        Log.d("HomeFragment", response.data.battery.toString())
                        if (response.data.transaction?.id != null) {
                            binding.ltLoading.visibility = View.GONE
                            binding.tvEmptySubs.visibility = View.GONE
                            binding.ltEmpty.visibility = View.GONE
                            transactionData = response.data.transaction

                            transactionData?.let { transaction ->
                                binding.apply {
                                    tvIdBattery.text = "EV${transaction.id}"
                                    tvIdBatteryType.text = if (transaction.rentTypeId == 1) "72V 20Ah Battery" else "72V 40Ah Battery"
                                    tvMotorcycleName.text = transaction.batteryName
                                }
                            }
                        } else {
                            binding.ltLoading.visibility = View.GONE
                            binding.tvEmptySubs.visibility = View.VISIBLE
                            binding.ltEmpty.visibility = View.VISIBLE
                            binding.cvLayoutDashboard.visibility = View.INVISIBLE
                        }

                        if(response.data.battery?.id != null) {
                            batteryData = response.data.battery
                            batteryData?.let { battery ->
                                binding.apply {
                                    cardTempMonitoring.tvCardPercentage.text = "${battery.suhu ?: "-"}%"
                                    cardTempMonitoring.tvCardEstimate.text = if(battery.suhu.isNullOrEmpty()) "Unknown Condition" else "Good Condition"
                                    cardConsumption.tvNumberSumary.text = "${battery.dayaDigunakan ?: "-"}"
                                    cardConsumption.tvSatuanSumary.text = "KwH"
                                    cardTime.tvNumberSumary.text = "${battery.arus ?: "-"}"
                                    cardTime.tvSatuanSumary.text = "A"
                                    cardDistance.tvNumberSumary.text = "${battery.tegangan ?: "-"}"
                                    cardDistance.tvSatuanSumary.text = "V"
                                    cardBatteryMonitoring.tvCardPercentage.text = "${battery.daya ?: "-"}°C"
                                }
                            }
                        } else {
                            binding.apply {
                                cardTempMonitoring.tvCardEstimate.text = "Unknown Condition"
                                cardConsumption.tvNumberSumary.text = "-"
                                cardTime.tvNumberSumary.text = "-"
                                cardDistance.tvNumberSumary.text = "-"
                                cardBatteryMonitoring.tvCardPercentage.text = "-"
                                cardTempMonitoring.tvCardPercentage.text = "-"
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
}