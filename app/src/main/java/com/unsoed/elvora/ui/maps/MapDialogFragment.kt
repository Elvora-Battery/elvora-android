package com.unsoed.elvora.ui.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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

        val name = arguments?.getString(NAME_STATION)
        val distance = arguments?.getDouble(DISTANCE_STATION, 0.0)

        binding.tvDistanceStation.text = "$distance km from your location"
        binding.tvStationName.text = name
    }

    companion object {
        const val NAME_STATION = "name_station"
        const val DISTANCE_STATION = "distance_station"
        const val TAG = "ModalMapBottomSheet"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}