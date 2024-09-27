package com.unsoed.elvora.ui.maps

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unsoed.elvora.data.response.map.StationsItem
import com.unsoed.elvora.databinding.FragmentMapDialogBinding

class MapDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentMapDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataStation = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable(EXTRA_DATA, StationsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(EXTRA_DATA)
        }

        dataStation?.let {
            binding.apply {
                tvDistanceStation.text = "${it.distance} km from your location"
                tvStationName.text = it.station
                tvStationStreet.text = it.address
                tvStationTime.text = it.status
            }
        }

        val connection = dataStation?.connections
        if (connection != null && connection.size == 3) {
            binding.tvTypeA.text = connection[0].type
            binding.tvValueTypeA.text = connection[0].power.toString()

            binding.tvTypeB.text = connection[1].type
            binding.tvValueTypeB.text = connection[1].power.toString()

            binding.tvTypeC.text = connection[2].type
            binding.tvValueTypeC.text = connection[2].power.toString()
        } else if(connection != null && connection.size == 2) {
            binding.tvTypeA.text = connection[0].type
            binding.tvValueTypeA.text = connection[0].power.toString()

            binding.tvTypeB.text = connection[1].type
            binding.tvValueTypeB.text = connection[1].power.toString()
        } else if(connection != null && connection.size == 1){
            binding.tvTypeA.text = connection[0].type
            binding.tvValueTypeA.text = connection[0].power.toString()
        }


        binding.btnDirection.setOnClickListener {
           (activity as MapsActivity).navigateToStation(dataStation?.latitude!!, dataStation?.longitude!!)
            dismiss()
        }

    }

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val TAG = "ModalMapBottomSheet"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}