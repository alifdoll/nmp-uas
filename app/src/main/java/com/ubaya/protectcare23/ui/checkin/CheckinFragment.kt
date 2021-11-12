package com.ubaya.protectcare23.ui.checkin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ubaya.protectcare23.R
import com.ubaya.protectcare23.databinding.FragmentCheckinBinding

class CheckinFragment : Fragment() {
    private lateinit var binding:FragmentCheckinBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCheckinBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}