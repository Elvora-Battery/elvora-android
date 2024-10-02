package com.unsoed.elvora.ui.home

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
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
import java.util.Locale
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels {
        HomeModelFactory.getInstance(requireContext())
    }
    private var userData: UserModel? = null
    private var isTierUserPremium: Boolean = false
    private var isSubsReminderActive: Boolean = false
    private var firstTimeGetReminder: Boolean = false
    private var batteryData: Battery? = null
    private var transactionData: Transaction? = null
    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

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
        workManager = WorkManager.getInstance(requireContext())

        initView()
        setupViewFragment()
        setupCardDriveTime()
        setupCardBattery()
        setupCardConsumption()
        setupCardDistance()
        setupCardTemperature()
    }

    private fun setDataSession(dataSession: Boolean) {
        Log.d(TAG, "Daily reminder: $dataSession")
        if (dataSession) {
            isSubsReminderActive = true
            Log.d(TAG, "Daily reminder subs aktif")
        } else {
            isSubsReminderActive = false
            Log.d(TAG, "Daily reminder subs tidak aktif")
            homeViewModel.setReminderSubs(true)
            startPeriodicTask()
        }
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

        homeViewModel.getReminderSubs()
        homeViewModel.isReminderSubsActive.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { dataSession ->
                setDataSession(dataSession)
            }

            binding.ivMember.setOnClickListener {
                val intent = Intent(requireContext(), ProfileActivity::class.java)
                intent.putExtra(ProfileActivity.EXTRA_DATA, userData)
                intent.putExtra(ProfileActivity.EXTRA_PREMIUM, isTierUserPremium)
                startActivity(intent)
            }

            binding.btnNotification.setOnClickListener {
                val intent = Intent(requireContext(), NotificationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun startPeriodicTask() {
        Log.d(TAG, "Periodic dijalankan")
        val data = Data.Builder()
            .putString(ReminderWorker.EXTRA_TOKEN, userData?.token)
            .build()

        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        periodicWorkRequest =
            PeriodicWorkRequest.Builder(ReminderWorker::class.java, 12, TimeUnit.HOURS)
                .setInputData(data)
                .setConstraints(constraint)
                .build()

        workManager.enqueue(periodicWorkRequest)
        workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id)
            .observe(viewLifecycleOwner) { workInfo ->
                if (workInfo.state == WorkInfo.State.ENQUEUED) {
                    Log.d(TAG, workInfo.state.name)
                }
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
            tvTitleSumary.text = "On Charging"
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
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT)
                            .show()
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
                                    tvIdBatteryType.text =
                                        if (transaction.rentTypeId == 1) "72V 20Ah Battery" else "72V 40Ah Battery"
                                    tvMotorcycleName.text = transaction.batteryName
                                }
                            }
                        } else {
                            binding.ltLoading.visibility = View.GONE
                            binding.tvEmptySubs.visibility = View.VISIBLE
                            binding.ltEmpty.visibility = View.VISIBLE
                            binding.cvLayoutDashboard.visibility = View.INVISIBLE
                        }

                        if (response.data.battery?.id != null) {
                            batteryData = response.data.battery
                            batteryData?.let { battery ->
                                binding.apply {
                                    cardTempMonitoring.tvCardPercentage.text =
                                        "${battery.suhu ?: "-"}°C"
                                    cardTempMonitoring.tvCardEstimate.text =
                                        if (battery.suhu == null) "Unknown Condition" else "Good Condition"
                                    cardConsumption.tvNumberSumary.text =
                                        "${String.format("%.2f", battery.dayaDigunakan) ?: "-"}"
                                    cardConsumption.tvSatuanSumary.text = "KwH"
                                    cardTime.tvNumberSumary.text =
                                        if (battery.chargingStatus == "Not Charging") "No" else "Yes"
                                    cardTime.tvSatuanSumary.text = ""
                                    cardDistance.tvNumberSumary.text =
                                        "${battery.distanceTravelled ?: "-"}"
                                    cardDistance.tvSatuanSumary.text = "Km"
                                    cardBatteryMonitoring.tvCardPercentage.text = "${
                                        String.format(
                                            "%.2f",
                                            battery.batteryPercentage
                                        ) ?: "-"
                                    }%"
                                }
                                getAddress(battery.latitude!!, battery.longitude!!)
                            }
                        } else {
                            binding.apply {
                                cardTempMonitoring.tvCardEstimate.text = "Unknown Condition"
                                cardConsumption.tvNumberSumary.text = "-"
                                cardTime.tvNumberSumary.text = "No"
                                cardDistance.tvNumberSumary.text = "-"
                                cardBatteryMonitoring.tvCardPercentage.text = "-"
                                cardTempMonitoring.tvCardPercentage.text = "-"
                            }
                        }
                    }

                    ApiResult.Empty -> {}
                }
            }
        }
    }

    private fun getAddress(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(latitude, longitude, 2) { list ->
                if (list.isNotEmpty()) {
                    val address = buildAddress(list[0])
                    if (address.isNotEmpty()) {
                        activity?.runOnUiThread {
                            binding.tvMemberLocation.text = address
                        }
                    } else {
                        activity?.runOnUiThread {
                            binding.tvMemberLocation.text = "Location is not found"
                            Toast.makeText(
                                requireContext(),
                                "Location Helper: address not found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun buildAddress(address: Address): String {
        val rtrwPattern = Regex("(RT\\.\\d+/RW\\.\\d+)")

        val addressLine = address.getAddressLine(0)
        val rtrwMatchResult = rtrwPattern.find(addressLine)

        val mRtRw = rtrwMatchResult?.value ?: ""
        Log.d("LocationStore", "RT/RW: $mRtRw")

        val premises =
            if (address.premises != null && address.premises.contains("Blok"))
                address.premises else "Blok ${address.premises}"

        val subFare =
            if (address.subThoroughfare != null && address.subThoroughfare.contains("No"))
                address.subThoroughfare else "No ${address.subThoroughfare}"

        val addresses = arrayOf(
            if (address.thoroughfare == null || address.thoroughfare.isEmpty()) "${address.featureName} $mRtRw" else "${address.thoroughfare} $mRtRw",
            if (address.premises == null || address.premises.isEmpty()) null else premises,
            if (address.subThoroughfare == null || address.subThoroughfare.isEmpty()) null else subFare
        )

        val builder = StringBuilder()
        for (addressString in addresses) {
            if (addressString.isNullOrEmpty()) {
                continue
            }
            if (builder.length + addressString.length > MAX_ADDRESS_LENGTH) {
                builder.substring(0, builder.length - 2)
                break
            }
            builder.append(addressString)
            if (builder.length + 2 > MAX_ADDRESS_LENGTH) {
                break
            }
            builder.append(", ")
        }

        val simpleAddress = builder.toString()
            .trim { it <= ' ' }

        return ("$simpleAddress ${address.subLocality ?: "-"}, ${address.locality ?: "-"}, ${address.subAdminArea ?: "-"}, ${address.adminArea ?: "-"}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "HomeFragment"
        const val MAX_ADDRESS_LENGTH = 75
    }
}