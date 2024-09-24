package com.unsoed.elvora.ui.subs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.unsoed.elvora.R
import com.unsoed.elvora.databinding.FragmentSubsBinding

class SubsFragment : Fragment() {

    private var _binding: FragmentSubsBinding? = null
    private val binding get() = _binding!!

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
    }

    private fun setupSubsInformation() {
        val shapeAppearanceModel = ShapeAppearanceModel.builder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 20f)
            .setBottomRightCorner(CornerFamily.ROUNDED, 20f)
            .build()

        val shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)

        binding.cvRemainSubs.apply {
            tvTitleSumary.text = "Remaining Subs"
            tvNumberSumary.text = "24"
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
            tvNumberSumary.text = "15"
            tvNumberSumary.setTextColor(requireActivity().getColor(R.color.blue_container2))
            tvTitleSumary.setTextColor(requireActivity().getColor(R.color.blue_container2))
            cvSummary.background = shapeDrawable2
        }
    }

}