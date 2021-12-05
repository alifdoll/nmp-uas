package com.ubaya.protectcare23.ui.checkin

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.ubaya.protectcare23.listener.OnCheckListener
import com.ubaya.protectcare23.api.ApiService
import com.ubaya.protectcare23.databinding.FragmentCheckinBinding
import com.ubaya.protectcare23.di.Injection
import com.ubaya.protectcare23.utils.Global
import kotlinx.coroutines.launch

class CheckinFragment : Fragment(), OnCheckListener {
    private lateinit var binding:FragmentCheckinBinding
    private lateinit var apiService: ApiService
    private lateinit var token: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCheckinBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiService = Injection.provideService(requireContext())
        token = requireActivity().getSharedPreferences(Global.PREFS_NAME, Context.MODE_PRIVATE)
            .getString(Global.PREFS_NAME, "").toString()

        viewLifecycleOwner.lifecycleScope.launch {
            val checks = apiService.checkStatus(token)
            Global.checkedIn = checks.checkOut == "null"
            Global.checks = checks
            val transaction = childFragmentManager.beginTransaction()
            val fragment = when {
                Global.checkedIn -> FragmentOut(this@CheckinFragment)
                else -> FragmentIn(this@CheckinFragment)
            }

            transaction.replace(binding.fragmentContainerCheckIn.id, fragment)
            transaction.commit()
        }

    }
    override fun OnButtonClicked() {
        viewLifecycleOwner.lifecycleScope.launch {
            val checks = apiService.checkStatus(token)
            Global.checks = checks
            val transaction = childFragmentManager.beginTransaction()
            val fragment = when {
                Global.checkedIn -> FragmentOut(this@CheckinFragment)
                else -> FragmentIn(this@CheckinFragment)
            }

            transaction.replace(binding.fragmentContainerCheckIn.id, fragment)
            transaction.commit()
        }
    }
}