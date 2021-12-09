package com.ubaya.protectcare23.ui.checkin

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.ubaya.protectcare23.listener.OnCheckListener
import com.ubaya.protectcare23.api.ApiService
import com.ubaya.protectcare23.databinding.FragmentOutBinding
import com.ubaya.protectcare23.di.Injection
import com.ubaya.protectcare23.utils.Global
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class FragmentOut(val listener: OnCheckListener) : Fragment() {


    private lateinit var binding: FragmentOutBinding
    private lateinit var apiService: ApiService
    private val mListener = listener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiService = Injection.provideService(requireContext())

        val token = requireActivity().getSharedPreferences(Global.PREFS_NAME, Context.MODE_PRIVATE)
            .getString(Global.PREFS_NAME, "").toString()

        val checks = Global.checks
        binding.txtLocation.setText(checks.loc_name)

        val parser = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss")

        binding.txtCheckinTime.setText(parser.format(checks.checkIn))

        binding.btnCheckout.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val chkOut = apiService.checkOut(
                    token,
                    checks.loc_id
                )

                Log.d("test out", checks.loc_id.toString())

                Global.checkedIn = !chkOut
                mListener.OnButtonClicked()
            }
        }

    }


}