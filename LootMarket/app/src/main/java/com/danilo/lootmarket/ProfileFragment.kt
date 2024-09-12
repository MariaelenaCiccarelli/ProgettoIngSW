package com.danilo.lootmarket


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.danilo.lootmarket.databinding.FragmentProfileBinding
import com.danilo.lootmarket.databinding.FragmentSearchBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)

        binding.textViewLabelInfoBusinessFrammentoProfile.isVisible = false
        binding.textViewLabelRagioneSocialeFrammentoProfile.isVisible = false
        binding.textViewValueRagioneSocialeFrammentoProfile.isVisible = false
        binding.textViewLabelPartitaIvaFrammentoProfile.isVisible = false
        binding.textViewValuePartivaIvaFrammentoProfile.isVisible = false
        binding.textViewLabelNumeroAziendaleFrammentoProfile.isVisible = false
        binding.cardEditTextNumeroAziendaleFrammentoProfile.isVisible = false
        binding.cardOverlayFrammentoProfile.isVisible = false

        binding.bottonePassaABusinessFrammentoProfile.setOnClickListener{
            binding.cardOverlayFrammentoProfile.isVisible = true
            binding.cardBottoneModificaFrammentoProfile.isVisible = false
        }

        binding.bottoneOverlayFrammentoProfile.setOnClickListener{




            binding.textViewLabelInfoBusinessFrammentoProfile.isVisible = true
            binding.textViewLabelRagioneSocialeFrammentoProfile.isVisible = true
            binding.textViewValueRagioneSocialeFrammentoProfile.isVisible = true
            binding.textViewLabelPartitaIvaFrammentoProfile.isVisible = true
            binding.textViewValuePartivaIvaFrammentoProfile.isVisible = true
            binding.textViewLabelNumeroAziendaleFrammentoProfile.isVisible = true
            binding.cardEditTextNumeroAziendaleFrammentoProfile.isVisible = true
            binding.cardBottonePassaABusinessFrammentoProfile.isVisible = false
            binding.cardOverlayFrammentoProfile.isVisible = false
            binding.cardBottoneModificaFrammentoProfile.isVisible = true

        }

        return binding.root
    }


}