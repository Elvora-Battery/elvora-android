package com.unsoed.elvora.ui.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.unsoed.elvora.R
import com.unsoed.elvora.databinding.FragmentHelpBinding

class HelpFragment : Fragment() {

    private var _binding: FragmentHelpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHelpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cvFaq.apply {
            tvTitleHelp.text = "FAQ"
            tvSubTitleHelp.text = "Frequently asked questions"
            ivHelp.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.icon_message))
        }

        binding.cvContact.apply {
            tvTitleHelp.text = "Contact Support"
            tvSubTitleHelp.text = "WA, email and telephone services"
            ivHelp.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.icon_telephone))
        }

        binding.cvTnc.apply {
            tvTitleHelp.text = "Terms and Conditions"
            tvSubTitleHelp.text = "Application terms and conditions asked questions"
            ivHelp.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.icon_warning))
        }

        binding.cvFaq.cvSubmitReport.setOnClickListener {
            val navigationToFaq = HelpFragmentDirections.actionNavigationHelpToFaqActivity()
            it.findNavController().navigate(navigationToFaq)
        }

        binding.cvTnc.cvSubmitReport.setOnClickListener {
            val navigationToTnc = HelpFragmentDirections.actionNavigationHelpToTnCActivity()
            it.findNavController().navigate(navigationToTnc)
        }

        binding.cvContact.cvSubmitReport.setOnClickListener {
            val navigationToContact = HelpFragmentDirections.actionNavigationHelpToContactSupportActivity()
            it.findNavController().navigate(navigationToContact)
        }
    }

}